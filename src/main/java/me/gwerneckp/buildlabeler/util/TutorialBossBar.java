package me.gwerneckp.buildlabeler.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static org.bukkit.Bukkit.getLogger;

/**
 * Utility class for managing tutorial Boss Bars for players.
 */
public class TutorialBossBar {

    /**
     * A HashMap to store active Boss Bars associated with player names.
     */
    public static HashMap<String, BossBar> activeBossBars = new HashMap<>();

    /**
     * Shows the tutorial Boss Bar to the specified player.
     *
     * @param player The player for whom to show the tutorial Boss Bar.
     */
    public static void show(Player player) {
        LanguageResources lr = LanguageResources.getInstance();
        BossBar bar = Bukkit.createBossBar(lr.getMessage(LanguageResources.Messages.TUTORIAL_BOSSBAR, player.getName()), BarColor.BLUE, BarStyle.SOLID);
        activeBossBars.put(player.getName(), bar);
        bar.addPlayer(player);
    }

    /**
     * Hides the tutorial Boss Bar from the specified player.
     *
     * @param player The player for whom to hide the tutorial Boss Bar.
     */
    public static void hide(Player player) {
        BossBar bar = activeBossBars.get(player.getName());
        if (bar != null) {
            bar.removePlayer(player);
        }
    }

    /**
     * Checks if the specified player has a tutorial Boss Bar active.
     * @param player
     * @return
     */
    public static boolean hasBossBar(Player player) {
        BossBar bar = activeBossBars.get(player.getName());
        return bar != null;
    }
}
