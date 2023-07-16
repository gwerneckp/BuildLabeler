package me.gwerneckp.buildlabeler;


import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import static org.bukkit.Bukkit.*;

public class CustomListener implements Listener {
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        getServer().broadcastMessage(event.getPlayer().getName() + " harvenesed " + event.getBlock().getType());
    }
}