package me.gwerneckp.buildlabeler.command.language;

import me.gwerneckp.buildlabeler.SessionManager;
import me.gwerneckp.buildlabeler.util.LanguageResources;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;

import static org.bukkit.Bukkit.getLogger;

public class LanguageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LanguageResources lr = LanguageResources.getInstance();

        if (!(sender instanceof Player)) {
            getLogger().info("You must be a player to run this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendRawMessage(lr.getMessage(LanguageResources.Messages.NEED_SPECIFY_LANGUAGE, player.getName()));
            return true;
        }


        String language = args[0];

        String[] validEnglish = {"en", "eng", "english", "inglês", "ingles", "ing", "americano"};
        String[] validPortuguese = {"pt", "pt-br", "portuguese", "português", "portugues", "br", "brasileiro"};
        String[] validFrench = {"fr", "français", "frances", "french", "francês", "france"};
        String[] validSpanish = {"es", "español", "espanhol", "spanish", "espanhol", "espanha", "argentina", "es-ar", "spain", "es-es"};

        String languageToSet = null;
        for (String valid : validEnglish) {
            if (valid.equalsIgnoreCase(language)) {
                languageToSet = "en";
                break;
            }
        }

        for (String valid : validPortuguese) {
            if (valid.equalsIgnoreCase(language)) {
                languageToSet = "pt";
                break;
            }
        }

        for (String valid : validFrench) {
            if (valid.equalsIgnoreCase(language)) {
                languageToSet = "fr";
                break;
            }
        }

        for (String valid : validSpanish) {
            if (valid.equalsIgnoreCase(language)) {
                languageToSet = "es";
                break;
            }
        }

        if (!(languageToSet == null)) {
            lr.setLanguage(player.getName(), languageToSet);
            player.sendRawMessage(lr.getMessage(LanguageResources.Messages.LANGUAGE_SET, player.getName()) + language + "!");

//            Hide and show tutorial bossbar
            if (TutorialBossBar.hasBossBar(player)) {
                TutorialBossBar.hide(player);
                TutorialBossBar.show(player);
            }

//            If in session, show label and help
            SessionManager sessionManager = SessionManager.getInstance();
            if (sessionManager.isPlayerInSession(player.getName())) {
                sessionManager.getSession(player.getName()).sendHelp();
                sessionManager.getSession(player.getName()).showLabel();
            }
            return true;
        }

        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.INVALID_LANGUAGE, player.getName()));
        return false;
    }

}
