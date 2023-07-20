package me.gwerneckp.buildlabeler.util;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;

public class Lobby {
    public ProtectedRegion region;
    public World world;

    public Lobby(ProtectedRegion region, World world) {
        this.region = region;
        this.world = world;
    }

    public String toString() {
        return "Lobby(region=" + region + ", world=" + world + ")";
    }
}
