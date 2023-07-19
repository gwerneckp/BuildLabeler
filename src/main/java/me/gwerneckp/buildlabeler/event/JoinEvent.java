package me.gwerneckp.buildlabeler.event;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getLogger;

public class JoinEvent implements Listener {
    private final SessionManager sessionManager;

    public JoinEvent(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TutorialBossBar.show(event.getPlayer());
    }

}
