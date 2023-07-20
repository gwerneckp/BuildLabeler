package me.gwerneckp.buildlabeler;

import me.gwerneckp.buildlabeler.command.*;
import me.gwerneckp.buildlabeler.command.language.LanguageCommand;
import me.gwerneckp.buildlabeler.command.session.*;
import me.gwerneckp.buildlabeler.event.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildLabeler extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Running BuildLabeler plugin");
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("clean").setExecutor(new CleanCommand());
        getCommand("end").setExecutor(new EndCommand());
        getCommand("label").setExecutor(new LabelCommand());
        getCommand("randomlabel").setExecutor(new RandomLabelCommand());
        getCommand("submit").setExecutor(new SubmitCommand());
        getCommand("language").setExecutor(new LanguageCommand());
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    }

    @Override
    public void onDisable() {
    }
}
