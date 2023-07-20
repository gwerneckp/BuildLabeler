package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import me.gwerneckp.buildlabeler.util.Schematic;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Random;


/**
 * Represents a building session for a player within a specific region.
 */
public class Session {

    private final Player player;
    private final ProtectedRegion sessionRegion;
    private String label = null;
    private BossBar bossBar = null;
    private LanguageResources lr = LanguageResources.getInstance();

    /**
     * Creates a new Session for the given player and region.
     *
     * @param player        The player associated with the session.
     * @param sessionRegion The region where the building session takes place.
     */
    public Session(Player player, ProtectedRegion sessionRegion) {
        this.player = player;
        this.sessionRegion = sessionRegion;
        sessionRegion.getMembers().addPlayer(player.getUniqueId());

        TutorialBossBar.hide(player);
        teleportAndGamemode();
        randomLabel();
        sendHelp();
    }

    /**
     * Sends a help message to the player, listing available commands for the building session.
     */
    public void sendHelp() {
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.LABEL_HELP, player.getName()));
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.RANDOM_LABEL_HELP, player.getName()));
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.END_HELP, player.getName()));
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.CLEAN_HELP, player.getName()));
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.SUBMIT_HELP, player.getName()));
    }

    private void teleportAndGamemode() {
        Vector3 middlePoint = sessionRegion.getParent().getMinimumPoint().add(sessionRegion.getMaximumPoint()).divide(2).toVector3();
        Location sessionLocation = new Location(player.getWorld(), middlePoint.getX(), sessionRegion.getMinimumPoint().getY() + 1, middlePoint.getZ());
        player.teleport(sessionLocation);

        player.setGameMode(org.bukkit.GameMode.CREATIVE);

//        give wooden axe
        player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
    }

    public void randomLabel() {
        LanguageResources.Labels[] availableLabels = {LanguageResources.Labels.TREE, LanguageResources.Labels.CASTLE, LanguageResources.Labels.HOUSE, LanguageResources.Labels.FARM};

        // Get a random label from the availableLabels array
        String randomLabel = lr.getLabel(availableLabels[new Random().nextInt(availableLabels.length)], player.getName());
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

    /**
     * Sets the label for the building session and displays it using a boss bar.
     *
     * @param label The label to set for the building session.
     */
    public void setLabel(String label) {
        this.label = label;
        showLabel();
    }

    /**
     * Retrieves the current label for the building session.
     *
     * @return The current label.
     */
    public String getLabel() {
        return label;
    }

    public void showLabel() {
        removeBar();
        bossBar = Bukkit.createBossBar(lr.getMessage(LanguageResources.Messages.BUILD_A_BOSSBAR, player.getName()) + label + "!", BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        bossBar.addPlayer(player);
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.BUILD_A, player.getName()) + label + "!");
    }

    private void removeBar() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public void submit() {
        saveSchematic();
        clean();
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.SUBMIT_SUCCESS, player.getName()));
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        randomLabel();
    }

    private void saveSchematic() {
        Location pos1 = new Location(player.getWorld(), sessionRegion.getMinimumPoint().getX(), sessionRegion.getMinimumPoint().getY(), sessionRegion.getMinimumPoint().getZ());
        Location pos2 = new Location(player.getWorld(), sessionRegion.getMaximumPoint().getX(), sessionRegion.getMaximumPoint().getY(), sessionRegion.getMaximumPoint().getZ());

        Schematic schematic = new Schematic(player.getWorld(), pos1, pos2);
        try {
            String schematicName = player.getName() + "_" + System.currentTimeMillis();
            schematic.saveNBT(label, schematicName);
        } catch (IOException e) {
            player.sendRawMessage(lr.getMessage(LanguageResources.Messages.ERROR_SAVING_SCHEMATIC, player.getName()) + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clean() {
        CuboidRegion region = new CuboidRegion(sessionRegion.getMinimumPoint(), sessionRegion.getMaximumPoint());
        region.iterator().forEachRemaining(blockVector3 -> {
            Location location = new Location(player.getWorld(), blockVector3.getX(), blockVector3.getY(), blockVector3.getZ());
            player.getWorld().getBlockAt(location).setType(Material.AIR);
        });

        // TODO: Get this to work with WorldEdit
    }

    /**
     * Retrieves the region associated with the building session.
     *
     * @return The protected region of the building session.
     */
    public ProtectedRegion getRegion() {
        return sessionRegion;
    }
}
