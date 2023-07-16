package me.gwerneckp.buildlabeler;

import org.bukkit.plugin.java.JavaPlugin;

public final class Build_labeler extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new CustomListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
