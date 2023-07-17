package me.gwerneckp.buildlabeler;

import static org.bukkit.Bukkit.getLogger;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;


public class Schematics {
    private final World world;
    private final Location pos1;
    private final Location pos2;
    public final JSONObject schematicData;

    public Schematics(World world, Location pos1, Location pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.schematicData = createSchematicData();
    }

    public void saveJson(String filename) {
        try {
            String jsonString = schematicData.toJSONString();
            saveToFile(filename, jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNBT(String filename) throws IOException {
        NBTContainer nbtContainer = new NBTContainer();
        NBTCompound schematicCompound = nbtContainer.addCompound("Schematic");

        schematicCompound.setInteger("Version", (Integer) schematicData.get("Version"));
        schematicCompound.setInteger("DataVersion", (Integer) schematicData.get("DataVersion"));

        JSONObject metadataObj = (JSONObject) schematicData.get("Metadata");
        NBTCompound metadataCompound = schematicCompound.addCompound("Metadata");
        metadataCompound.setInteger("WEOffsetX", (int) metadataObj.get("WEOffsetX"));
        metadataCompound.setInteger("WEOffsetY", (int) metadataObj.get("WEOffsetY"));
        metadataCompound.setInteger("WEOffsetZ", (int) metadataObj.get("WEOffsetZ"));

//        Short width = ((Integer) schematicData.get("Width")).shortValue();
//        getLogger().info("Width: " + width + " Type: " + width.getClass().getName());

        schematicCompound.setShort("Width", ((Integer) schematicData.get("Width")).shortValue());
        schematicCompound.setShort("Height", ((Integer) schematicData.get("Height")).shortValue());
        schematicCompound.setShort("Length", ((Integer) schematicData.get("Length")).shortValue());

        JSONArray offsetArr = (JSONArray) schematicData.get("Offset");
        int[] offsetIntArray = new int[offsetArr.size()];
        for (int i = 0; i < offsetArr.size(); i++) {
            offsetIntArray[i] = (int) offsetArr.get(i);
        }
        schematicCompound.setIntArray("Offset", offsetIntArray);

        JSONObject paletteObj = (JSONObject) schematicData.get("Palette");
        NBTCompound paletteCompound = schematicCompound.addCompound("Palette");
        for (Object key : paletteObj.keySet()) {
            String keyStr = (String) key;
            int value = (int) paletteObj.get(keyStr);
            paletteCompound.setInteger(keyStr, value);
        }

        schematicCompound.setInteger("PaletteMax", (Integer) schematicData.get("PaletteMax"));

        JSONArray blockDataArr = (JSONArray) schematicData.get("BlockData");
        byte[] blockDataByteArray = new byte[blockDataArr.size()];
        for (int i = 0; i < blockDataArr.size(); i++) {
            blockDataByteArray[i] = (byte) (int) blockDataArr.get(i);
        }
        NBTCompound blockDataCompound = schematicCompound.addCompound("BlockData");
        blockDataCompound.setByteArray("Data", blockDataByteArray);
        // If you have any block entities, you can add them here.
        // blockEntitiesCompound.addCompound("BlockEntityTag").setString("someKey", "someValue");

        NBTFile.saveTo(new File(filename), nbtContainer);
    }


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
            metadataObj.put("WEOffsetX", -minX);
            metadataObj.put("WEOffsetY", -minY);
            metadataObj.put("WEOffsetZ", -minZ);
            schematicObj.put("Metadata", metadataObj);

            schematicObj.put("Width", width);
            schematicObj.put("Height", height);
            schematicObj.put("Length", length);
            schematicObj.put("Offset", new JSONArray());

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

    private int getDataVersion() {
        // You could get this dynamically, but I did not find a way to do it and I do not need it for my use case. If someone else ever needs to use this, feel free to implement it.
        return 3465;
    }

    private void saveToFile(String filename, String content) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename))) {
            bos.write(content.getBytes());
        }
    }
}
