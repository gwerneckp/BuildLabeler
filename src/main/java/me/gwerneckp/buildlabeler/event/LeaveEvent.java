package me.gwerneckp.buildlabeler.event;

import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getLogger;

public class LeaveEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SessionManager sessionManager = SessionManager.getInstance();
        getLogger().info("Player " + event.getPlayer().getName() + " left the game. Ending their build session.");
        if (sessionManager.isPlayerInSession(event.getPlayer().getName())) {
            sessionManager.endSession(event.getPlayer().getName());
        }
    }

}
