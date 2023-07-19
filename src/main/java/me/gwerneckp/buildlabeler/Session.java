package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gwerneckp.buildlabeler.util.Schematics;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Session {

    private final Player player;
    private ProtectedRegion sessionRegion;
    private String label = null;
    private BossBar bossBar = null;

    public Session(Player player, ProtectedRegion sessionRegion) {
        this.player = player;
        this.sessionRegion = sessionRegion;
        sessionRegion.getMembers().addPlayer(player.getUniqueId());

        TutorialBossBar.hide(player);
        teleportAndGamemode();
        randomLabel();
        sendHelp();
    }

    public void sendHelp() {
        player.sendRawMessage("""       
                %s/label <label>:%s Set a new label.
                %s/randomlabel:%s Get a new random label.
                %s/end:%s End the building session.
                %s/clean:%s Clean the building area.
                %s/submit%s: Submit your build.
                    """.formatted(
                ChatColor.GOLD,
                ChatColor.DARK_AQUA,
                ChatColor.GOLD,
                ChatColor.DARK_AQUA,
                ChatColor.GOLD,
                ChatColor.DARK_AQUA,
                ChatColor.GOLD,
                ChatColor.DARK_AQUA,
                ChatColor.GOLD,
                ChatColor.DARK_AQUA
        ));
    }

    private void teleportAndGamemode() {
        Vector3 middlePoint = sessionRegion.getParent().getMinimumPoint().add(sessionRegion.getMaximumPoint()).divide(2).toVector3();
        Location sessionLocation = new Location(player.getWorld(), middlePoint.getX(), middlePoint.getY(), middlePoint.getZ());
        player.teleport(sessionLocation);

        player.setGameMode(org.bukkit.GameMode.CREATIVE);
    }


    public void randomLabel() {
        String[] availableLabels = {"tree", "house", "castle", "bridge"};

        // Get a random label from the availableLabels array

        String randomLabel = availableLabels[new Random().nextInt(availableLabels.length)];
        if (randomLabel.equals(label)) {
            randomLabel();
            return;
        }

        setLabel(randomLabel);
    }

    public void endSession() {
        clean();
        sessionRegion.getMembers().removePlayer(player.getUniqueId());
        player.teleport(player.getWorld().getSpawnLocation());
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.getInventory().clear();
        removeBar();
        TutorialBossBar.show(player);
    }

    public void setLabel(String label) {
        this.label = label;
        showLabel();
    }

    public String getLabel() {
        return label;
    }


    private void showLabel() {
        removeBar();
        bossBar = Bukkit.createBossBar("Build a " + label + "!", BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
        bossBar.addPlayer(player);
        player.sendRawMessage(ChatColor.DARK_AQUA + "Build a " + ChatColor.GOLD + label + ChatColor.DARK_AQUA + "!");
    }

    private void removeBar() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public void submit() {
        saveSchematic();
        clean();
        randomLabel();
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    private void saveSchematic() {
        Location pos1 = new Location(player.getWorld(), sessionRegion.getMinimumPoint().getX(), sessionRegion.getMinimumPoint().getY(), sessionRegion.getMinimumPoint().getZ());
        Location pos2 = new Location(player.getWorld(), sessionRegion.getMaximumPoint().getX(), sessionRegion.getMaximumPoint().getY(), sessionRegion.getMaximumPoint().getZ());

        Schematics schematic = new Schematics(player.getWorld(), pos1, pos2);
        //        Filename constructed by playername + label + timestamp
        try {
            String schematicName = player.getName() + "_" + label + "_" + System.currentTimeMillis();
            schematic.saveNBT(player.getName() + "_" + label + "_" + System.currentTimeMillis());
        } catch (IOException e) {
            player.sendRawMessage(ChatColor.RED + "Error saving schematic" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clean() {
        CuboidRegion region = new CuboidRegion(sessionRegion.getMinimumPoint(), sessionRegion.getMaximumPoint());
        region.iterator().forEachRemaining(blockVector3 -> {
            Location location = new Location(player.getWorld(), blockVector3.getX(), blockVector3.getY(), blockVector3.getZ());
            player.getWorld().getBlockAt(location).setType(Material.AIR);
        });

//     TODO: Get this to work with WorldEdit
    }


    public ProtectedRegion getRegion() {
        return sessionRegion;
    }
}
