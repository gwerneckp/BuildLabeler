package me.gwerneckp.buildlabeler.util;

import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public enum Messages {
    NO_SESSION(ChatColor.RED + "You are not in a build session. Use " + ChatColor.WHITE + "/build to start one.", ChatColor.RED + "Você não está em uma sessão de construção. " + ChatColor.WHITE + "Use /construir para iniciar uma."),
    SESSION_ENDED(ChatColor.DARK_AQUA + "Your session has ended!", ChatColor.DARK_AQUA + "Sua sessão foi encerrada!"),
    MUST_PROVIDE_LABEL(ChatColor.RED + "You must provide a label." + ChatColor.WHITE + " Use /label <label>", ChatColor.RED + "Você deve fornecer um tema." + ChatColor.WHITE + " Use /tema <tema>"),
    SESSION_CLEANED(ChatColor.DARK_AQUA + "Cleaned area!", ChatColor.DARK_AQUA + "Área limpa!"),
    SUBMIT_SUCCESS(ChatColor.DARK_AQUA + "Build submitted!", ChatColor.DARK_AQUA + "Construção submetida!"),
    BUILD_A(ChatColor.DARK_AQUA + "Build a(n) ", ChatColor.DARK_AQUA + "Construa um(a) "),
    BUILD_A_BOSSBAR("Build a(n) ", "Construa um(a) "),
    TUTORIAL_BOSSBAR("Type /build to start building!", "Digite /construir para começar a construir!"),
    ERROR_SAVING_SCHEMATIC(ChatColor.RED + "Error saving schematic.", ChatColor.RED + "Erro ao salvar o esquemático."),
    LABEL_HELP(ChatColor.GOLD + "/label <label>:" + ChatColor.DARK_AQUA + " Set a new label.", ChatColor.GOLD + "/tema <tema>:" + ChatColor.DARK_AQUA + " Escolha um novo tema."),
    RANDOM_LABEL_HELP(ChatColor.GOLD + "/randomlabel:" + ChatColor.DARK_AQUA + " Get a new random label.", ChatColor.GOLD + "/temaaleatorio:" + ChatColor.DARK_AQUA + " Receba um novo tema aleatório."),
    END_HELP(ChatColor.GOLD + "/end:" + ChatColor.DARK_AQUA + " End the building session.", ChatColor.GOLD + "/terminar:" + ChatColor.DARK_AQUA + " Encerre a sessão de construção."),
    CLEAN_HELP(ChatColor.GOLD + "/clean:" + ChatColor.DARK_AQUA + " Clean the building area.", ChatColor.GOLD + "/limpar:" + ChatColor.DARK_AQUA + " Limpe a área de construção."),
    SUBMIT_HELP(ChatColor.GOLD + "/submit:" + ChatColor.DARK_AQUA + " Submit your build.", ChatColor.GOLD + "/submeter:" + ChatColor.DARK_AQUA + " Submeta sua construção.");


    private final String english;
    private final String portuguese;
    public final static HashMap<String, String> userLanguages = loadLanguages();

    private static HashMap<String, String> loadLanguages() {
        HashMap<String, String> languages = new HashMap<>();

        try {
            // Load the YAML file using SnakeYAML
            Yaml yaml = new Yaml();
            String pathToYamlFile = "plugins/buildlabeler/player_languages.yml";
            FileInputStream fileInputStream = new FileInputStream(pathToYamlFile);

            // Parse the YAML file into a Map
            Map<String, String> data = yaml.load(fileInputStream);

            // Add the data from the Map to the HashMap
            if (data != null) {
                languages.putAll(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return languages;
    }

    Messages(String english, String portuguese) {
        this.english = english;
        this.portuguese = portuguese;
    }

    public String getEnglish() {
        return english;
    }

    public String getPortuguese() {
        return portuguese;
    }

    public String toStringI18N(String playerName) {
        String language = (String) userLanguages.get(playerName);

        if (language == null) {
            return english;
        }

        if (language.equals("pt")) {
            return portuguese;
        }

        return english;
    }
}
