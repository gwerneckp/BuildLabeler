package me.gwerneckp.buildlabeler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildLabeler extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Running BuildLabeler plugin");
        getCommand("savetest").setExecutor(new SaveTest());
    }

    @Override
    public void onDisable() {
    }
}
