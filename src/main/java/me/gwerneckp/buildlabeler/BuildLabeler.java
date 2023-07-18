package me.gwerneckp.buildlabeler;

import org.bukkit.plugin.java.JavaPlugin;

public final class BuildLabeler extends JavaPlugin {
    SessionManager sessionManager = null;

    @Override
    public void onEnable() {
        getLogger().info("Running BuildLabeler plugin");
        sessionManager = new SessionManager();
        getCommand("build").setExecutor(new BuildCommand(sessionManager));
        getCommand("end").setExecutor(new EndCommand(sessionManager));
    }

    @Override
    public void onDisable() {
    }
}
