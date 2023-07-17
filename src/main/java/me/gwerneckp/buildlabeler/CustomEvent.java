package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;


public class CustomEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
//        getLogger().info(Region.class.getName());
        Location location = event.getBlock().getLocation();
        Location pos1 = new Location(location.getWorld(), -35, 40, -54);
        Location pos2 = new Location(location.getWorld(), -34, 41, -49);

        Schematics schematics = new Schematics(Objects.requireNonNull(location.getWorld()), pos1, pos2);
        schematics.saveSchematic("teste.json");
        getLogger().info("Should have saved the schematic...");
    }
}
