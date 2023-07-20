package me.gwerneckp.buildlabeler.command.session;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;

import org.bukkit.entity.Player;

public class RandomThemeCommand extends SessionExecutor {
    @Override
    protected boolean executeCommand(Player player, SessionManager sessionManager, LanguageResources lr, String[] args) {
        sessionManager.getSession(player.getName()).randomLabel();
        return true;
    }
}
