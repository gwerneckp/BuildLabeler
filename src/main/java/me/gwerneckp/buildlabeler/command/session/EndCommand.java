package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import org.bukkit.entity.Player;

public class EndCommand extends SessionExecutor {
    @Override
    protected boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args) {
        sessionManager.endSession(player.getName());
        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.SESSION_ENDED, player.getName()));
        return true;
    }
}
