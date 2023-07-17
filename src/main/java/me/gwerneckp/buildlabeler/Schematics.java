package me.gwerneckp.buildlabeler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static org.bukkit.Bukkit.getLogger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Schematics {
    private final World world;
    private final Location pos1;
    private final Location pos2;
    public JSONObject schematicData;

    public Schematics(World world, Location pos1, Location pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.schematicData = createSchematicData();
    }

    public void saveSchematic(String filename) {
        try {
            String jsonString = schematicData.toJSONString();
            saveToFile(filename, jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            JSONObject rootObj = new JSONObject();
            rootObj.put("Schematic", schematicObj);

            return rootObj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getDataVersion() {
        //  You could get this dynamically, but I did not find a way to do it and I do not need it for my use case. If someone else ever needs to use this, feel free to implement it.
        return 3465;
    }

    private void saveToFile(String filename, String json) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filename)))) {
            bos.write(json.getBytes());
        }
    }
}
