package me.gwerneckp.buildlabeler;

import com.sk89q.worldguard.protection.managers.storage.StorageException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.Bukkit.getLogger;

public class EndCommand implements CommandExecutor {
    private final SessionManager sessionManager;

    EndCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

//        check if sender is a player
        if (!(sender instanceof org.bukkit.entity.Player)) {
            return false;
        } else {
//            Check if player has a session
            if (sessionManager.isPlayerInSession(sender.getName())) {
                sessionManager.endSession(sender.getName());
            } else {
            }
        }
        return true;
    }
}
