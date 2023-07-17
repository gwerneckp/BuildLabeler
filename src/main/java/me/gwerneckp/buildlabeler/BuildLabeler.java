package me.gwerneckp.buildlabeler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildLabeler extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new CustomEvent(), this);
    }

    @Override
    public void onDisable() {
    }
}
