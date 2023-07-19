package me.gwerneckp.buildlabeler.util;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class TutorialBossBar {
    public static void show(Player player) {
        BossBar bar = Bukkit.getBossBar(NamespacedKey.minecraft("spawn_tutorial"));
        if (bar != null) {
            bar.addPlayer(player);
        }
    }

    public static void hide(Player player) {
        BossBar bar = Bukkit.getBossBar(NamespacedKey.minecraft("spawn_tutorial"));
        if (bar != null) {
            bar.removePlayer(player);
        }
    }
}
