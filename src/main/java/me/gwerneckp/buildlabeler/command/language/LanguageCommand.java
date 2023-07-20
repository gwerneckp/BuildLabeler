package me.gwerneckp.buildlabeler.command.language;

import me.gwerneckp.buildlabeler.util.LanguageResources;
import me.gwerneckp.buildlabeler.util.TutorialBossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getLogger;

public class LanguageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LanguageResources lr = LanguageResources.getInstance();
        if (args.length > 1) {
            if (!(sender instanceof Player)) {
                ((Player) sender).sendRawMessage(lr.getMessage(LanguageResources.Messages.NEED_SPECIFY_LANGUAGE, sender.getName()));
            }
            return false;
        }

        String playername = null;
        String language = null;
        Player player = null;
        if (!(sender instanceof Player)) {
            if (args.length < 2) {
                getLogger().info("You must provide a player name when running from server! /language <player> <language>");
                return false;
            }

            playername = args[0];
            language = args[1];
        } else {
            playername = sender.getName();
            language = args[0];
            player = (Player) sender;
        }

        String[] validEnglish = {"en", "eng", "english", "inglês", "ingles", "ing", "americano"};
        String[] validPortuguese = {"pt", "pt-br", "portuguese", "português", "portugues", "br", "brasileiro"};
        for (String valid : validEnglish) {
            if (valid.equalsIgnoreCase(language)) {
                lr.setLanguage(playername, "en");
                player.sendRawMessage(lr.getMessage(LanguageResources.Messages.LANGUAGE_SET, player.getName()) + "english!");
                return true;
            }
        }

        for (String valid : validPortuguese) {
            if (valid.equalsIgnoreCase(language)) {
                lr.setLanguage(playername, "pt");
                player.sendRawMessage(lr.getMessage(LanguageResources.Messages.LANGUAGE_SET, player.getName()) + "português!");
                return true;
            }
        }

        player.sendRawMessage(lr.getMessage(LanguageResources.Messages.INVALID_LANGUAGE, player.getName()));
        return false;
    }

}
