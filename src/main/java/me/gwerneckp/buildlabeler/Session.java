package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

import static org.bukkit.Bukkit.getLogger;

public class Session {

    private final Player player;
    private ProtectedRegion sessionRegion;
    private String label = null;

    public Session(Player player, ProtectedRegion sessionRegion) throws StorageException {
        this.player = player;
        this.sessionRegion = sessionRegion;
        sessionRegion.getMembers().addPlayer(player.getUniqueId());

        teleportAndGamemode();
        newLabel();
    }

    private void teleportAndGamemode() {
        Vector3 middlePoint = sessionRegion.getParent().getMinimumPoint().add(sessionRegion.getMaximumPoint()).divide(2).toVector3();
        Location sessionLocation = new Location(player.getWorld(), middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
        player.teleport(sessionLocation);

        player.setGameMode(org.bukkit.GameMode.CREATIVE);
    }


    public void newLabel() throws StorageException {
        String[] availableLabels = {"tree", "house", "castle", "bridge"};


        // Get a random label from the availableLabels array
        Random random = new Random();
        String label = availableLabels[random.nextInt(availableLabels.length)];

        setLabel(label);
    }

    public void endSession() {
//        Bukkit.broadcastMessage("TODO: remove blocks from session region");
        sessionRegion.getMembers().removePlayer(player.getUniqueId());
        player.teleport(player.getWorld().getSpawnLocation());
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    }

    private void showLabel() {
        player.sendRawMessage("You need to build a " + label + "!\n You can use /label <label> to change the label of your build, or /newlabel to get a new random label.");
    }

    public void setLabel(String label) {
        this.label = label;
        showLabel();
    }

    public String getLabel() {
        return label;
    }

    public ProtectedRegion getRegion() {
        return sessionRegion;
    }
}
