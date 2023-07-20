package me.gwerneckp.buildlabeler.util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a schematic generator for building structures and saving them as JSON and NBT files.
 */
public class Schematic {

    private final World world;
    private final Location pos1;
    private final Location pos2;
    public final JSONObject schematicData;

    /**
     * Constructs a new Schematic instance.
     *
     * @param world The world in which the schematic is generated.
     * @param pos1  The first corner of the region to be saved as a schematic.
     * @param pos2  The second corner of the region to be saved as a schematic.
     */
    public Schematic(World world, Location pos1, Location pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.schematicData = createSchematicData();
    }

    /**
     * Saves the schematic data to a JSON file.
     *
     * @param filename The name of the JSON file to save the schematic data.
     */
    public void saveJson(String filename) {
        try {
            String jsonString = schematicData.toJSONString();
            saveToFile(filename, jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the schematic data to an NBT file.
     *
     * @param path     The directory path to save the NBT file.
     * @param filename The name of the NBT file to save the schematic data.
     * @throws IOException If an error occurs while saving the NBT file.
     */
    public void saveNBT(String path, String filename) throws IOException {
        NBTContainer container = new NBTContainer();

        container.setString("name", "Schematic");

        container.setInteger("Version", (Integer) schematicData.get("Version"));
        container.setInteger("DataVersion", (Integer) schematicData.get("DataVersion"));

        JSONObject metadataObj = (JSONObject) schematicData.get("Metadata");
        NBTCompound metadataCompound = container.addCompound("Metadata");
        metadataCompound.setInteger("WEOffsetX", (int) metadataObj.get("WEOffsetX"));
        metadataCompound.setInteger("WEOffsetY", (int) metadataObj.get("WEOffsetY"));
        metadataCompound.setInteger("WEOffsetZ", (int) metadataObj.get("WEOffsetZ"));

        container.setShort("Width", ((Integer) schematicData.get("Width")).shortValue());
        container.setShort("Height", ((Integer) schematicData.get("Height")).shortValue());
        container.setShort("Length", ((Integer) schematicData.get("Length")).shortValue());

        JSONObject paletteObj = (JSONObject) schematicData.get("Palette");
        NBTCompound paletteCompound = container.addCompound("Palette");
        for (Object key : paletteObj.keySet()) {
            String keyStr = (String) key;
            int value = (int) paletteObj.get(keyStr);
            paletteCompound.setInteger(keyStr, value);
        }

        container.setInteger("PaletteMax", (Integer) schematicData.get("PaletteMax"));

        JSONArray blockDataArr = (JSONArray) schematicData.get("BlockData");
        byte[] blockDataByteArray = new byte[blockDataArr.size()];
        for (int i = 0; i < blockDataArr.size(); i++) {
            blockDataByteArray[i] = (byte) (int) blockDataArr.get(i);
        }
        container.setByteArray("BlockData", blockDataByteArray);

        NBTFile.saveTo(new File("plugins/buildlabeler/schematics/" + path, filename), (NBTCompound) container);
    }

    /**
     * Creates the schematic data in JSON format.
     *
     * @return The JSON object representing the schematic data.
     */
    private JSONObject createSchematicData() {
        JSONObject schematicObj = new JSONObject();

        try {
            int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
            int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
            int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
            int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
            int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
            int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

            int width = maxX - minX + 1;
            int height = maxY - minY + 1;
            int length = maxZ - minZ + 1;

            schematicObj.put("Version", 2);
            schematicObj.put("DataVersion", getDataVersion());

            JSONObject metadataObj = new JSONObject();
            metadataObj.put("WEOffsetX", 0);
            metadataObj.put("WEOffsetY", 0);
            metadataObj.put("WEOffsetZ", 0);
            schematicObj.put("Metadata", metadataObj);

            schematicObj.put("Width", width);
            schematicObj.put("Height", height);
            schematicObj.put("Length", length);

            JSONArray blockDataArr = new JSONArray();
            JSONObject paletteObj = new JSONObject();
            int index = 0;
            Map<String, Integer> blockPalette = new HashMap<>();

            // Loop through the blocks within the region
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int x = minX; x <= maxX; x++) {
                        Block block = world.getBlockAt(x, y, z);
                        String blockState = block.getBlockData().getAsString();

                        if (!blockPalette.containsKey(blockState)) {
                            blockPalette.put(blockState, index);
                            paletteObj.put(blockState, index);
                            index++;
                        }

                        blockDataArr.add(blockPalette.get(blockState));
                    }
                }
            }

            schematicObj.put("BlockData", blockDataArr);
            schematicObj.put("Palette", paletteObj);
            schematicObj.put("PaletteMax", index);
            schematicObj.put("BlockEntities", new JSONArray());

            return schematicObj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the data version for the schematic. (Note: For demonstration purposes, a static value is returned.)
     *
     * @return The data version for the schematic.
     */
    private int getDataVersion() {
        // You could get this dynamically, but it's not implemented in this example.
        // Feel free to implement a dynamic method to get the correct data version for your use case.
        return 3465;
    }

    /**
     * Saves the content to a file with the specified filename.
     *
     * @param filename The name of the file to save the content.
     * @param content  The content to be saved to the file.
     * @throws IOException If an error occurs while saving the file.
     */
    private void saveToFile(String filename, String content) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename))) {
            bos.write(content.getBytes());
        }
    }
}
