package me.gwerneckp.buildlabeler.command.worlds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class GetWorldsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendRawMessage(Bukkit.getWorlds().toString());
            return true;
        }

        getLogger().info(Bukkit.getWorlds().toString());
        return true;
    }
}
