package me.gwerneckp.buildlabeler.util;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;

/**
 * Represents a lobby with its associated region and world in Bukkit.
 */
public class Lobby {

    /**
     * The protected region associated with the lobby.
     */
    public ProtectedRegion region;

    /**
     * The world in which the lobby exists.
     */
    public World world;

    /**
     * Creates a new Lobby instance with the given region and world.
     *
     * @param region The protected region associated with the lobby.
     * @param world  The world in which the lobby exists.
     */
    public Lobby(ProtectedRegion region, World world) {
        this.region = region;
        this.world = world;
    }

    /**
     * Returns a string representation of the Lobby object.
     * The string will be in the format: "Lobby(region=REGION_NAME, world=WORLD_NAME)".
     *
     * @return A string representation of the Lobby object.
     */
    @Override
    public String toString() {
        return "Lobby(region=" + region + ", world=" + world + ")";
    }
}
