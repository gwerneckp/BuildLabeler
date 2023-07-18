package me.gwerneckp.buildlabeler;

import com.sk89q.worldguard.protection.managers.storage.StorageException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.Bukkit.getLogger;

public class BuildCommand implements CommandExecutor {
    private final SessionManager sessionManager;

    BuildCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

//        check if sender is a player
        if (!(sender instanceof org.bukkit.entity.Player)) {
            Bukkit.broadcastMessage("You must be a player to use this command");
            return false;
        } else {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
            try {
                sessionManager.newSession(player);
                Bukkit.broadcastMessage("New session created");
                Bukkit.broadcastMessage("Session: " + sessionManager.getSession(player.getName()));

            } catch (StorageException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
