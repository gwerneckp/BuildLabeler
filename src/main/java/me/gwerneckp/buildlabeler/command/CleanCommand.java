package me.gwerneckp.buildlabeler.command;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class CleanCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        check if sender is a player
        if (!(sender instanceof Player)) {
            return false;
        }

        SessionManager sessionManager = SessionManager.getInstance();

//            Check if player has a session
        LanguageResources lr = LanguageResources.getInstance();
        if (!sessionManager.isPlayerInSession(sender.getName())) {
            ((Player) sender).sendRawMessage(lr.getMessage(LanguageResources.Messages.NO_SESSION, sender.getName()));
            return false;
        }

        sessionManager.getSession(sender.getName()).clean();
        ((Player) sender).sendRawMessage(lr.getMessage(LanguageResources.Messages.SESSION_CLEANED, sender.getName()));
        return true;
    }
}
