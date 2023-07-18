package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gwerneckp.buildlabeler.util.Schematics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

import java.io.IOException;
import java.util.Random;

public class Session {

    private final Player player;
    private ProtectedRegion sessionRegion;
    private String label = null;

    public Session(Player player, ProtectedRegion sessionRegion) {
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


    public void newLabel() {
        String[] availableLabels = {"tree", "house", "castle", "bridge"};


        // Get a random label from the availableLabels array
        Random random = new Random();
        String label = availableLabels[random.nextInt(availableLabels.length)];

        setLabel(label);
    }

    public void endSession() {
        clear();
        sessionRegion.getMembers().removePlayer(player.getUniqueId());
        player.teleport(player.getWorld().getSpawnLocation());
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    }

    private void showLabel() {
        player.sendRawMessage("""
                %sBuild a %s%s!%s   
                If you want to build something else, type %s/label <label>%s to get a new label.
                If you want another random label, type %s/newlabel.%s
                Type %s/end%s to end the session.
                    """.formatted(
                ChatColor.GREEN,
                ChatColor.YELLOW,
                label,
                ChatColor.GREEN,
                ChatColor.YELLOW,
                ChatColor.GREEN,
                ChatColor.YELLOW,
                ChatColor.GREEN,
                ChatColor.YELLOW,
                ChatColor.GREEN
        ));
    }

    public void submit() {
        saveSchematic();
        clear();
        newLabel();
        showLabel();
    }

    private void saveSchematic() {
        Location pos1 = new Location(player.getWorld(), sessionRegion.getMinimumPoint().getX(), sessionRegion.getMinimumPoint().getY(), sessionRegion.getMinimumPoint().getZ());
        Location pos2 = new Location(player.getWorld(), sessionRegion.getMaximumPoint().getX(), sessionRegion.getMaximumPoint().getY(), sessionRegion.getMaximumPoint().getZ());

        Schematics schematic = new Schematics(player.getWorld(), pos1, pos2);
        //        Filename constructed by playername + label + timestamp
        try {
            schematic.saveNBT("papaiviado.schematic");
//            schematic.saveNBT(player.getName() + "_" + label + "_" + System.currentTimeMillis());
        } catch (IOException e) {
            player.sendRawMessage(ChatColor.RED + "Error saving schematic" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        CuboidRegion region = new CuboidRegion(sessionRegion.getMinimumPoint(), sessionRegion.getMaximumPoint());
        region.iterator().forEachRemaining(blockVector3 -> {
            Location location = new Location(player.getWorld(), blockVector3.getX(), blockVector3.getY(), blockVector3.getZ());
            player.getWorld().getBlockAt(location).setType(Material.AIR);
        });

//        TODO: Get this to work with WorldEdit
//        getLogger().info("Clearing region: " + region);
//        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
//        EditSession editSession = WorldEdit.getInstance().newEditSession(region.getWorld());
//        try {
//            editSession.setBlocks(region, BlockTypes.COBBLESTONE.getDefaultState().toBaseBlock());
//            Operations.complete(editSession.commit());
//        } catch (WorldEditException e) {
//            throw new RuntimeException(e);
//        }
//        EditSession editSession = WorldEdit.getInstance().newEditSession(region.getWorld());
//        BaseBlock air = Objects.requireNonNull(BlockTypes.AIR).getDefaultState().toBaseBlock();
//
//        try {
//            editSession.setBlocks(region, air );
//            editSession.commit();
//
//        } catch (MaxChangedBlocksException e) {
//            throw new RuntimeException(e);
//        }
////        try {
////            getLogger().info("Setting blocks to air");
////            editSession.setBlocks(region, air);
////        } catch (MaxChangedBlocksException e) {
////            throw new RuntimeException(e);
//        }

        getLogger().info("Flushing session");


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
