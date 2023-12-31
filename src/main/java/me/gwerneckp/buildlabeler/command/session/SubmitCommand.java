package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class SubmitCommand extends SessionExecutor {
    @Override
    protected boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args) {
        sessionManager.getSession(player.getName()).submit();
        return true;
    }
}
