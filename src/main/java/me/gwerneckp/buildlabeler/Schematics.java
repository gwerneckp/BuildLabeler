package me.gwerneckp.buildlabeler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Schematics {
    private final World world;
    private final Location pos1;
    private final Location pos2;

    public Schematics(World world, Location pos1, Location pos2) {
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public void saveSchematic(String filename) {
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

            JSONObject rootObj = new JSONObject();
            JSONObject schematicObj = new JSONObject();

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
            schematicObj.put("BlockEntities", new JSONArray());

            JSONArray blockDataArr = new JSONArray();
            JSONObject paletteObj = new JSONObject();

            int index = 0;
            Map<String, Integer> blockPalette = new HashMap<>();

            // Loop through the blocks within the region
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int x = minX; x <= maxX; x++) {
                        Block block = world.getBlockAt(x, y, z);
                        Material material = block.getType();
                        String blockState = getBlockStateAsString(block);

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

            rootObj.put("Schematic", schematicObj);

            // Save the schematic in JSON format
            saveToFile(filename, rootObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBlockStateAsString(Block block) {
        Material material = block.getType();
        StringBuilder blockStateBuilder = new StringBuilder(material.getKey().toString());

//        if (block.getType().hasBlockData()) {
//            // For blocks with properties, such as stairs, slabs, etc.
//            // You need to handle each property accordingly
//            // Assuming we are dealing with stairs for demonstration purposes
//            blockStateBuilder.append("[");
//            // Replace this with the actual block properties handling
//            blockStateBuilder.append("facing=").append("north");
//            blockStateBuilder.append(",").append("half=").append("bottom");
//            blockStateBuilder.append("]");
//        }

        return blockStateBuilder.toString();
    }

    private int addBlockStateToPalette(JSONObject blocksObj, String blockState) {
        int index = blocksObj.size();
        blocksObj.put(blockState, index);
        return index;
    }

    // ... Implement Biomes, Entities, and other methods ...
    private int getDataVersion() {
        // You can implement this method to return the correct DataVersion
        // for your supported Minecraft version.
        // For example, for Minecraft 1.12.2, the DataVersion is 1343.
        // Make sure to handle the appropriate DataVersion based on the Minecraft version you support.
        // Return the appropriate DataVersion for your Minecraft version.

        // For demonstration purposes, assuming we support Minecraft 1.12.2
        return 1343;
    }

    private void saveToFile(String filename, String json) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filename)))) {
            bos.write(json.getBytes());
        }
    }
}
