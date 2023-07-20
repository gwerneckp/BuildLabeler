package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class CleanCommand extends SessionExecutor {
    @Override
    protected boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args) {
        sessionManager.getSession(player.getName()).clean();
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.SESSION_CLEANED, player.getName()));
        return true;
    }
}