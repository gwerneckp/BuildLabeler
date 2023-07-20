package me.gwerneckp.buildlabeler.command;

import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;


public class BuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

//        check if sender is a player
        if (!(sender instanceof Player)) {
            getLogger().info("You must be a player to use this command");
            return false;
        }

        Player player = (Player) sender;

        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.newSession(player);
        return true;
    }
}
