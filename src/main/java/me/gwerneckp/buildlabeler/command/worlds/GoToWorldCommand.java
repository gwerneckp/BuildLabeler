package me.gwerneckp.buildlabeler.command.worlds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoToWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 1) {
                if (p.getServer().getWorld(args[0]) == null) {
                    p.sendRawMessage("World not found");
                    return false;
                }

                p.teleport(p.getServer().getWorld(args[0]).getSpawnLocation());
                return true;
            }
        }

        return false;
    }
}
