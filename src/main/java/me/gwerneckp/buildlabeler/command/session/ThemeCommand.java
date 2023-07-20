package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.util.LanguageResources;

import me.gwerneckp.buildlabeler.SessionManager;
import org.bukkit.entity.Player;

public class ThemeCommand extends SessionExecutor {
    @Override
    protected boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args) {
        if (args.length == 0) {
            player.sendRawMessage(lr.getMessage(LanguageResources.Messages.MUST_PROVIDE_LABEL, player.getName()));
            return true;
        }

        sessionManager.getSession(player.getName()).setLabel(args[0]);
        return true;
    }
}
