package me.gwerneckp.buildlabeler.util;

import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getLogger;

/**
 * A utility class for managing lobby worlds in Bukkit.
 */
public class LobbyWorld {

    private static final Pattern WORLD_NUMBER_PATTERN = Pattern.compile("^lobbies/build_world_(\\d+)$");
    private static final Pattern LOBBY_WORLD_PATTERN = Pattern.compile("^build_world[_\\d]*$");


    /**
     * Loads all lobby worlds found in the "lobbies" directory.
     */
    public static void loadLobbies() {
        Server bukkitServer = Bukkit.getServer();
        String directory = "lobbies";
        File lobbiesDirectory = new File(".", directory);

        if (!lobbiesDirectory.exists() || !lobbiesDirectory.isDirectory()) {
            getLogger().warning("Lobbies directory not found!");
            return;
        }

        // Iterate through the files in the "lobbies" directory
        for (File worldFolder : lobbiesDirectory.listFiles()) {
            String worldName = worldFolder.getName();
            getLogger().info("Loading lobby world: " + worldName);
            Matcher matcher = LOBBY_WORLD_PATTERN.matcher(worldName);
            if (matcher.matches()) {
                getLogger().info("MATCHED: " + worldName);
                WorldCreator wc = new WorldCreator(directory + '/' + worldName);
                wc.createWorld();
            }
        }
    }

    /**
     * Creates a new lobby world by copying the template world and incrementing the world number.
     *
     * @return The newly created lobby world.
     */
    public static World createLobbyWorld() {
        World templateWorld = Bukkit.getWorld("lobbies/build_world");
        if (templateWorld == null) {
            getLogger().info("Template world not found");
            return null;
        }

        String newWorldName = "lobbies/build_world_" + getNextId();

        // Get the folder paths for the template and new world
        File templateWorldFolder = templateWorld.getWorldFolder();
        File newLobbyWorldFolder = new File(".", newWorldName);

        try {
            // Copy all files from the template world folder to the new world folder
            copyWorldFiles(templateWorldFolder.toPath(), newLobbyWorldFolder.toPath());

        } catch (IOException e) {
            getLogger().severe("Error copying world files: " + e.getMessage());
        }

        WorldCreator wc = new WorldCreator(newWorldName);
        // Create the new lobby world and return it
        wc.createWorld();

        WorldGuard worldGuard = WorldGuard.getInstance();
//        Copy regions from template world to new world
        worldGuard.getPlatform().getRegionContainer().get(worldGuard.getPlatform().getMatcher().getWorldByName("lobbies/build_world"))
                .getRegions().forEach((name, region) -> {
                    worldGuard.getPlatform().getRegionContainer().get(worldGuard.getPlatform().getMatcher().getWorldByName(newWorldName))
                            .addRegion(region);
                });


        return Bukkit.getWorld(newWorldName);
    }

    /**
     * Retrieves the next available lobby world number by searching through the existing worlds.
     *
     * @return The next available lobby world number.
     */
    private static Integer getNextId() {
        int lastLobbyWorldNumber = 0;
        for (World world : Bukkit.getWorlds()) {
            String worldName = world.getName();
            Matcher matcher = WORLD_NUMBER_PATTERN.matcher(worldName);
            if (matcher.matches()) {
                int worldNumber = Integer.parseInt(matcher.group(1));
                lastLobbyWorldNumber = Math.max(lastLobbyWorldNumber, worldNumber);
            }
        }
        return lastLobbyWorldNumber + 1;
    }

    // Helper method to copy world folder files recursively
    private static void copyWorldFiles(Path source, Path destination) throws IOException {
        Files.walk(source)
                .forEach(sourcePath -> {
                    try {
                        // Exclude copying the uid.dat file
                        if (!sourcePath.endsWith("uid.dat")) {
                            Path destinationPath = destination.resolve(source.relativize(sourcePath));
                            Files.createDirectories(destinationPath.getParent());
                            Files.copy(sourcePath, destinationPath);
                        }
                    } catch (IOException e) {
                        getLogger().severe("Error copying file: " + e.getMessage());
                    }
                });
    }
}
