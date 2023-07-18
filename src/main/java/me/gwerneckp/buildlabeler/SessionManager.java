package me.gwerneckp.buildlabeler;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.bukkit.Bukkit.getLogger;

public class SessionManager {
    private HashSet availableRegions = new HashSet<ProtectedRegion>();
    private HashSet usedRegions = new HashSet<ProtectedRegion>();
    private HashMap sessions = new HashMap<String, Session>();

    public SessionManager() {
//        World world = WorldGuard.getInstance().getPlatform().getMatcher().getWorldByName("world");
//        Region[] regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(world).getRegions().values().toArray(new Region[0]);
//        for (Region region : regions) {
//            if (region.getId().startsWith("lobby_")) {
//                availableLobbies.add(region.getCenter());
//            }
//        }

        WorldGuard worldGuard = WorldGuard.getInstance();
        ProtectedRegion lobby1 = worldGuard.getPlatform().getRegionContainer().get(worldGuard.getPlatform().getMatcher().getWorldByName("world")).getRegion("lobby_1_building");
        availableRegions.add(lobby1);
    }

    public void newSession(Player player) throws StorageException {
        ProtectedRegion sessionRegion = getAvailableLobby();

        Session session = new Session(player, sessionRegion);
        sessions.put(player.getName(), session);

        // Remove the lobby from the availableLobbies set
        availableRegions.remove(sessionRegion);
        // Add the lobby to the usedLobbies set
        usedRegions.add(sessionRegion);

    }

    public void endSession(String playerName) {
        Session session = (Session) sessions.get(playerName);
        session.endSession();
        // Remove the lobby from the usedLobbies set
        usedRegions.remove(session.getRegion());

        // Add the lobby to the availableLobbies set
        availableRegions.add(session.getRegion());

        // Remove the session from the sessions map
        sessions.remove(playerName);
    }

    public Session getSession(String playerName) {
        return (Session) sessions.get(playerName);
    }

    public Boolean isPlayerInSession(String playerName) {
        return sessions.containsKey(playerName);
    }

    private ProtectedRegion getAvailableLobby() {
        return (ProtectedRegion) availableRegions.toArray()[0];
    }

    public HashSet getAvailableRegions() {
        return availableRegions;
    }

    public HashSet getUsedRegions() {
        return usedRegions;
    }
}
