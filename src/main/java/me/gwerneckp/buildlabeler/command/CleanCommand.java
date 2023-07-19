package me.gwerneckp.buildlabeler.command;

import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class CleanCommand implements CommandExecutor {
    private final SessionManager sessionManager;

    public CleanCommand(SessionManager sessionManager) {
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

        sessionManager.getSession(sender.getName()).clean();
        ((Player) sender).sendRawMessage(ChatColor.DARK_AQUA + "Session cleaned!");
        return true;
    }
}
