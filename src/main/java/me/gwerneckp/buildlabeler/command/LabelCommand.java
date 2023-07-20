package me.gwerneckp.buildlabeler.command;

import me.gwerneckp.buildlabeler.util.Messages;
import org.bukkit.ChatColor;
import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class LabelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        check if sender is a player
        if (!(sender instanceof Player)) {
            return false;
        }

        SessionManager sessionManager = SessionManager.getInstance();

//            Check if player has a session
        if (!sessionManager.isPlayerInSession(sender.getName())) {
            ((Player) sender).sendRawMessage(Messages.NO_SESSION.toStringI18N(sender.getName()));
            return false;
        }

        if (args.length == 0) {
            ((Player) sender).sendRawMessage(Messages.MUST_PROVIDE_LABEL.toStringI18N(sender.getName()));
            return false;
        }

        sessionManager.getSession(sender.getName()).setLabel(args[0]);
        return true;
    }
}
