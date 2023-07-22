package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import me.gwerneckp.buildlabeler.util.Lobby;
import me.gwerneckp.buildlabeler.util.Schematic;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.io.IOException;
import java.util.Random;


/**
 * Represents a building session for a player within a specific region.
 */
public class Session {

    private final Player player;
    private final Lobby lobby;
    private final ProtectedRegion sessionRegion;
    private final World world;
    private String label = null;
    private LanguageResources.Labels labelTranslation = null;
    private BossBar bossBar = null;
    private LanguageResources lr = LanguageResources.getInstance();

    /**
     * Creates a new Session for the given player and region.
     *
     * @param player The player associated with the session.
     * @param lobby  The lobby associated with the session.
     */
    public Session(Player player, Lobby lobby) {
        this.player = player;
        this.lobby = lobby;
        this.sessionRegion = lobby.region;
        this.world = lobby.world;

        sessionRegion.getMembers().addPlayer(player.getUniqueId());

        TutorialBossBar.hide(player);
        teleportAndGamemode();
        randomLabel();
        sendHelp();
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.WORLDEDIT_ENABLED_BUILDING_AREA, player.getName()));
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

    /**
     * Teleports the player to the middle of the building area and sets their gamemode to creative.
     * The player is also given a wooden axe for WorldEdit.
     */
    private void teleportAndGamemode() {
        Vector3 middlePoint = sessionRegion.getParent().getMinimumPoint().add(sessionRegion.getMaximumPoint()).divide(2).toVector3();
        Location sessionLocation = new Location(world, middlePoint.getX(), sessionRegion.getMinimumPoint().getY() + 1, middlePoint.getZ());
        player.teleport(sessionLocation);

        player.setGameMode(org.bukkit.GameMode.CREATIVE);

//        give wooden axe
        player.getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
    }

    /**
     * Ends the building session for the player.
     * Building area is cleaned.
     * The player is teleported to the spawn location and the boss bar is removed.
     * The player's gamemode is set to survival and their inventory is cleared.
     * The player is added to the tutorial boss bar.
     */
    public void endSession() {
        clean();
        sessionRegion.getMembers().removePlayer(player.getUniqueId());
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.getInventory().clear();
        removeBar();
        TutorialBossBar.show(player);
    }

    /**
     * Gets a random label from the available labels and sets it for the building session.
     */
    public void randomLabel() {
        LanguageResources.Labels[] availableLabels = LanguageResources.Labels.values();

        // Get a random label from the availableLabels array
        LanguageResources.Labels randomLabel = availableLabels[new Random().nextInt(availableLabels.length)];
        if (randomLabel.getEnglish().equals(label)) {
            randomLabel();
            return;
        }

        setLabel(randomLabel);
    }

    /**
     * Sets the label for the building session and displays it using a boss bar.
     *
     * @param label The label to set for the building session.
     */
    public void setLabel(LanguageResources.Labels label) {
        this.label = label.getEnglish();
        this.labelTranslation = label;
        showLabel();
    }

    /**
     * Sets the label for the building session and displays it using a boss bar.
     *
     * @param label The label to set for the building session.
     */
    public void setLabel(String label) {
        this.label = label;
        this.labelTranslation = null;
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

    /**
     * Displays the label of the building session using a boss bar and sends a message to the player.
     * The label is translated if a translation is available. (For labels suggested by the server)
     */
    public void showLabel() {
        String labelToDisplay;
        if (labelTranslation != null) {
            labelToDisplay = lr.getLabel(labelTranslation, player.getName());
        } else {
            labelToDisplay = label;
        }

        removeBar();
        bossBar = Bukkit.createBossBar(lr.getMessage(LanguageResources.Messages.BUILD_A_BOSSBAR, player.getName()) + ChatColor.GOLD + labelToDisplay + "!", BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        bossBar.addPlayer(player);
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.BUILD_A, player.getName()) + labelToDisplay + "!");
    }

    /**
     * Removes the boss bar associated with the building session.
     */
    private void removeBar() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    /**
     * Submits the building session, saves the schematic, and prepares for a new session.
     */
    public void submit() {
        saveSchematic();
        clean();
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.SUBMIT_SUCCESS, player.getName()));
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        randomLabel();
    }

    /**
     * Saves the schematic of the building session.
     * The schematic is saved in English, regardless of the player's language. If it is a label suggested by the server, it will not be translated.
     */
    private void saveSchematic() {
        Location pos1 = new Location(lobby.world, sessionRegion.getMinimumPoint().getX(), sessionRegion.getMinimumPoint().getY(), sessionRegion.getMinimumPoint().getZ());
        Location pos2 = new Location(lobby.world, sessionRegion.getMaximumPoint().getX(), sessionRegion.getMaximumPoint().getY(), sessionRegion.getMaximumPoint().getZ());

        Schematic schematic = new Schematic(lobby.world, pos1, pos2);
        try {
            String schematicName = player.getName() + "_" + System.currentTimeMillis();
            schematic.saveNBT(label, schematicName);
        } catch (IOException e) {
            player.sendRawMessage(lr.getMessage(LanguageResources.Messages.ERROR_SAVING_SCHEMATIC, player.getName()) + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleans the region by removing all blocks within the building session.
     */
    public void clean() {
        CuboidRegion region = new CuboidRegion(sessionRegion.getMinimumPoint(), sessionRegion.getMaximumPoint());
        region.iterator().forEachRemaining(blockVector3 -> {
            Location location = new Location(lobby.world, blockVector3.getX(), blockVector3.getY(), blockVector3.getZ());
            player.getWorld().getBlockAt(location).setType(Material.AIR);
        });
    }

    /**
     * Retrieves the region associated with the building session.
     *
     * @return The protected region of the building session.
     */
    public Lobby getLobby() {
        return lobby;
    }
}
