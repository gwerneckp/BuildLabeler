package me.gwerneckp.buildlabeler.command.worlds;

import me.gwerneckp.buildlabeler.util.LobbyWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class CreateLobbyWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String worldName = LobbyWorld.createLobbyWorld().toString();
        if (sender instanceof Player) {
            ((Player) sender).sendRawMessage("Created lobby world: " + worldName);
        }
        return true;
    }
}
