package me.gwerneckp.buildlabeler.util;

import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

/**
 * Utility class to manage language resources for translations.
 */
public class LanguageResources {
    // Private constructor to prevent instantiation from other classes
    private LanguageResources() {
    }


    private static LanguageResources instance = null;


    /**
     * Retrieves the singleton instance of LanguageResources.
     *
     * @return The LanguageResources instance.
     */
    public static LanguageResources getInstance() {
        if (instance == null) {
            return new LanguageResources();
        }
        return instance;
    }

    /**
     * Enum representing different messages with English and Portuguese translations.
     */
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
        SUBMIT_HELP(ChatColor.GOLD + "/submit:" + ChatColor.DARK_AQUA + " Submit your build.", ChatColor.GOLD + "/submeter:" + ChatColor.DARK_AQUA + " Submeta sua construção."),
        NEED_SPECIFY_LANGUAGE(ChatColor.RED + "You must specify a language. Use /language <language>", ChatColor.RED + "Você deve especificar um idioma. Use /idioma <idioma>"),
        LANGUAGE_SET(ChatColor.DARK_AQUA + "Language set to ", ChatColor.DARK_AQUA + "Idioma definido para "),
        INVALID_LANGUAGE(ChatColor.RED + "Invalid language. Use /language <language>", ChatColor.RED + "Idioma inválido. Use /idioma <idioma>");

        private final String english;
        private final String portuguese;

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
    }

    /**
     * Enum representing different labels with English and Portuguese translations.
     */
    public enum Labels {
        TREE("Tree", "Árvore"),
        HOUSE("House", "Casa"),
        CASTLE("Castle", "Castelo"),
        FARM("Farm", "Fazenda");

        private final String english;
        private final String portuguese;

        Labels(String english, String portuguese) {
            this.english = english;
            this.portuguese = portuguese;
        }

        public String getEnglish() {
            return english;
        }

        public String getPortuguese() {
            return portuguese;
        }

    }

    public static final HashMap<String, String> userLanguages = loadLanguages();

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

    public void setLanguage(String playerName, String language) {
        getLogger().warning("NOT IMPLEMENTED");
//        TODO: Make this work
        userLanguages.put(playerName, language);
        getLogger().info("Setting language for " + playerName + " to " + language);
        getLogger().info("userLanguages: " + userLanguages);

        // Update the YAML file
        try {
            // Load the existing data from the YAML file into a Map
            Yaml yaml = new Yaml();
            String pathToYamlFile = "plugins/buildlabeler/player_languages.yml";
            FileInputStream fileInputStream = new FileInputStream(pathToYamlFile);
            Map<String, String> data = yaml.load(fileInputStream);

            // If data is null, create a new HashMap
            if (data == null) {
                data = new HashMap<>();
            }

            // Update the data with the new language preference
            data.put(playerName, language);

            // Write the updated data back to the YAML file
            try (FileOutputStream fileOutputStream = new FileOutputStream(pathToYamlFile)) {
                String yamlData = createYamlData(data);
                fileOutputStream.write(yamlData.getBytes());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Helper method to create a valid YAML string from the data map
    private String createYamlData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            sb.append(entry.getKey()).append(": \"").append(entry.getValue()).append("\"\n");
        }
        return sb.toString();
    }

    /**
     * Gets the language preference for the given player.
     *
     * @param playerName The name of the player.
     * @return The language preference of the player, defaulting to "en" if not found.
     */
    public String getLanguage(String playerName) {
        return userLanguages.getOrDefault(playerName, "en");
    }

    /**
     * Gets the translated message for the given message enum and player's language preference.
     *
     * @param message    The message enum representing the message.
     * @param playerName The name of the player to get their language preference.
     * @return The translated message based on the player's language preference.
     */
    public String getMessage(Messages message, String playerName) {
        String language = getLanguage(playerName);
        getLogger().info("Language for " + playerName + ": " + language);
        if (language.equals("pt")) {
            return message.getPortuguese();
        }

        return message.getEnglish();
    }

    /**
     * Gets the translated label for the given label enum and player's language preference.
     *
     * @param label      The label enum representing the label.
     * @param playerName The name of the player to get their language preference.
     * @return The translated label based on the player's language preference.
     */
    public String getLabel(Labels label, String playerName) {
        String language = getLanguage(playerName);
        if (language.equals("pt")) {
            return label.getPortuguese();
        }

        return label.getEnglish();
    }
}