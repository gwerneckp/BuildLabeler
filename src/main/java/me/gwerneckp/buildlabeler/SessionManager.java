package me.gwerneckp.buildlabeler;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.gwerneckp.buildlabeler.util.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private final HashSet<Lobby> availableLobbies = new HashSet<>();
    private final HashSet<Lobby> usedLobbies = new HashSet<>();
    private final HashMap<String, Session> sessions = new HashMap<>();
    private static SessionManager instance = null;

    /**
     * Creates a new SessionManager and initializes available regions.
     */
    private SessionManager() {
        WorldGuard worldGuard = WorldGuard.getInstance();
//        get all lobbies from lobbies/build_world_x
        Bukkit.getWorlds().forEach(world -> {
            worldGuard.getPlatform().getRegionContainer().get(worldGuard.getPlatform().getMatcher().getWorldByName(world.getName()))
                    .getRegions().forEach((name, region) -> {
                        if (name.matches("lobby_\\d+_building")) {
                            availableLobbies.add(new Lobby(region, world));
                        }
                    });
        });
    }

    /**
     * Gets the instance of the SessionManager (Singleton pattern).
     *
     * @return The SessionManager instance.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Starts a new building session for the given player.
     *
     * @param player The player starting the session.
     */
    public void newSession(Player player) {
        Lobby sessionLobby = getAvailableLobby();
        if (sessionLobby == null) {
            player.sendRawMessage(ChatColor.RED + "No available lobbies found.");
            return;
        }

        Session session = new Session(player, sessionLobby);
        sessions.put(player.getName(), session);

        // Move the region from available to used
        availableLobbies.remove(sessionLobby);
        usedLobbies.add(sessionLobby);
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
        usedLobbies.remove(session.getLobby());
        availableLobbies.add(session.getLobby());

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

    /**
     * Retrieves the available regions for building sessions.
     *
     * @return The set of available regions.
     */
    public Set<Lobby> getAvailableLobbies() {
        return Collections.unmodifiableSet(availableLobbies);
    }

    /**
     * Retrieves the used regions currently occupied by building sessions.
     *
     * @return The set of used regions.
     */
    public Set<Lobby> getUsedLobbies() {
        return Collections.unmodifiableSet(usedLobbies);
    }

    private Lobby getAvailableLobby() {
        if (availableLobbies.isEmpty()) {
            getLogger().warning("No available regions for a new session.");
            return null;
        }
        return availableLobbies.iterator().next();
    }
}
