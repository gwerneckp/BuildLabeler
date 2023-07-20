package me.gwerneckp.buildlabeler.command;

import com.sk89q.worldedit.function.pattern.RandomPattern;
import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        check if sender is a player
        if (!(sender instanceof Player)) {
            return false;
        }

        SessionManager sessionManager = SessionManager.getInstance();
        LanguageResources lr = LanguageResources.getInstance();
//            Check if player has a session
        if (!sessionManager.isPlayerInSession(sender.getName())) {
//            ((Player) sender).sendRawMessage(ChatColor.RED + "You don't have a session! " + ChatColor.WHITE + "/build to start one.");
            ((Player) sender).sendRawMessage(lr.getMessage(LanguageResources.Messages.NO_SESSION, sender.getName()));
            return false;
        }

        sessionManager.endSession(sender.getName());
        ((Player) sender).sendRawMessage(lr.getMessage(LanguageResources.Messages.SESSION_ENDED, sender.getName()));
        return true;
    }
}
