package me.gwerneckp.buildlabeler;

import me.gwerneckp.buildlabeler.command.*;
import me.gwerneckp.buildlabeler.event.LeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildLabeler extends JavaPlugin {
    SessionManager sessionManager = null;

    @Override
    public void onEnable() {
        getLogger().info("Running BuildLabeler plugin");
        sessionManager = new SessionManager();
        getCommand("build").setExecutor(new BuildCommand(sessionManager));
        getCommand("clear").setExecutor(new ClearCommand(sessionManager));
        getCommand("end").setExecutor(new EndCommand(sessionManager));
        getCommand("label").setExecutor(new LabelCommand(sessionManager));
        getCommand("newlabel").setExecutor(new NewLabelCommand(sessionManager));
        getCommand("submit").setExecutor(new SubmitCommand(sessionManager));
        getServer().getPluginManager().registerEvents(new LeaveEvent(sessionManager), this);
    }

    @Override
    public void onDisable() {
    }
}
