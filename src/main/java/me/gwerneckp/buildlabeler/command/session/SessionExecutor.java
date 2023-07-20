package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public abstract class SessionExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // check if sender is a player
        if (!(sender instanceof Player)) {
            getLogger().info("You must be a player to use this command");
            return true;
        }

        SessionManager sessionManager = SessionManager.getInstance();
        LanguageResources lr = LanguageResources.getInstance();

        // Check if player has a session
        if (!sessionManager.isPlayerInSession(sender.getName())) {
            handleNoSession((Player) sender, lr);
            return true;
        }

        // Call the abstract method to handle command-specific logic
        return executeCommand((Player) sender, sessionManager, lr, args);
    }

    /**
     * Method to handle the command-specific logic for the implementing class.
     *
     * @param player         The player who executed the command.
     * @param sessionManager The SessionManager instance.
     * @param lr             The LanguageResources instance.
     * @param args           The command arguments.
     * @return True if the command was executed successfully, false otherwise.
     */
    protected abstract boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args);

    /**
     * Handle the case when the player has no active session.
     *
     * @param player The player who executed the command.
     * @param lr     The LanguageResources instance.
     */
    private void handleNoSession(Player player, LanguageResources lr) {
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.NO_SESSION, player.getName()));
    }
}
