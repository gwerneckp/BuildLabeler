package me.gwerneckp.buildlabeler.event;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getLogger;

public class JoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SessionManager sessionManager = SessionManager.getInstance();
        event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
        event.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
        event.getPlayer().getInventory().clear();
        TutorialBossBar.show(event.getPlayer());
        LanguageResources lr = LanguageResources.getInstance();
        event.getPlayer().sendRawMessage(lr.getMessage(LanguageResources.Messages.WELCOME, event.getPlayer().getName()));
        event.getPlayer().sendRawMessage(lr.getMessage(LanguageResources.Messages.LANGUAGE_HELP, event.getPlayer().getName()));
    }

}
