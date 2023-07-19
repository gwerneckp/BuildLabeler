package me.gwerneckp.buildlabeler;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getLogger;

/**
 * Manages building sessions and available regions for players.
 */
public class SessionManager {
    private final HashSet<ProtectedRegion> availableRegions = new HashSet<>();
    private final HashSet<ProtectedRegion> usedRegions = new HashSet<>();
    private final HashMap<String, Session> sessions = new HashMap<>();

    /**
     * Creates a new SessionManager and initializes available regions.
     */
    public SessionManager() {
        WorldGuard worldGuard = WorldGuard.getInstance();
        ProtectedRegion lobby1 = worldGuard.getPlatform().getRegionContainer()
                .get(worldGuard.getPlatform().getMatcher().getWorldByName("world"))
                .getRegion("lobby_1_building");
        if (lobby1 != null) {
            availableRegions.add(lobby1);
        } else {
            getLogger().warning("Unable to find the lobby_1_building region.");
        }
    }

    /**
     * Starts a new building session for the given player.
     *
     * @param player The player starting the session.
     */
    public void newSession(Player player) {
        ProtectedRegion sessionRegion = getAvailableRegion();
        if (sessionRegion == null) {
            getLogger().warning("No available regions for a new session.");
            return;
        }

        Session session = new Session(player, sessionRegion);
        sessions.put(player.getName(), session);

        // Move the region from available to used
        availableRegions.remove(sessionRegion);
        usedRegions.add(sessionRegion);
    }

    /**
     * Ends the building session for the given player.
     *
     * @param playerName The name of the player whose session is ending.
     */
    public void endSession(String playerName) {
        Session session = sessions.get(playerName);
        if (session == null) {
            getLogger().warning("No session found for player: " + playerName);
            return;
        }

        session.endSession();
        // Move the region from used to available
        usedRegions.remove(session.getRegion());
        availableRegions.add(session.getRegion());

        // Remove the session from the sessions map
        sessions.remove(playerName);
    }

    /**
     * Retrieves the building session for the given player.
     *
     * @param playerName The name of the player.
     * @return The player's building session or null if not found.
     */
    public Session getSession(String playerName) {
        return sessions.get(playerName);
    }

    /**
     * Checks if a player is currently in a building session.
     *
     * @param playerName The name of the player.
     * @return True if the player is in a session, false otherwise.
     */
    public boolean isPlayerInSession(String playerName) {
        return sessions.containsKey(playerName);
    }

    private ProtectedRegion getAvailableRegion() {
        if (availableRegions.isEmpty()) {
            getLogger().warning("No available regions for a new session.");
            return null;
        }
        return availableRegions.iterator().next();
    }

    /**
     * Retrieves the set of available regions for building sessions.
     *
     * @return The set of available regions.
     */
    public Set<ProtectedRegion> getAvailableRegions() {
        return Collections.unmodifiableSet(availableRegions);
    }

    /**
     * Retrieves the set of used regions currently occupied by building sessions.
     *
     * @return The set of used regions.
     */
    public Set<ProtectedRegion> getUsedRegions() {
        return Collections.unmodifiableSet(usedRegions);
    }
}
