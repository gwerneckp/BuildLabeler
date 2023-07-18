package me.gwerneckp.buildlabeler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class SaveTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Location location = sender.getServer().getPlayer(sender.getName()).getLocation();
//        Location pos1 = new Location(location.getWorld(), -7, 44, -7);
        Location pos1 = new Location(location.getWorld(), -35, 40, -54);
//        Location pos2 = new Location(location.getWorld(), 9, 37, 9);
        Location pos2 = new Location(location.getWorld(), -34, 41, -49);

        Schematics schematics = new Schematics(Objects.requireNonNull(location.getWorld()), pos1, pos2);

        String filename = "new_try.schematic";

        try {
            schematics.saveNBT(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bukkit.broadcastMessage("Saved schematic to " + filename + "!");

        return true;
    }
}
