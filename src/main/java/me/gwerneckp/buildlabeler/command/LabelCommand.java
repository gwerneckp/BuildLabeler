package me.gwerneckp.buildlabeler.command;

import org.bukkit.ChatColor;
import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class LabelCommand implements CommandExecutor {
    private final SessionManager sessionManager;

    public LabelCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        check if sender is a player
        if (!(sender instanceof org.bukkit.entity.Player)) {
            return false;
        }
//            Check if player has a session
        if (!sessionManager.isPlayerInSession(sender.getName())) {
            ((Player) sender).sendRawMessage(ChatColor.RED + "You don't have a session! Use" + ChatColor.WHITE + "/build to start one.");

            return false;
        }

        if (args.length == 0) {
            ((Player) sender).sendRawMessage(ChatColor.RED + "You must provide a label." + ChatColor.GREEN + " Use /label <label>");
            return false;
        }

        sessionManager.getSession(sender.getName()).setLabel(args[0]);
        return true;
    }
}
