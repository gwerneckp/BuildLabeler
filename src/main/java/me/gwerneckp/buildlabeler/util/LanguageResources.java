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
     * Enum representing different messages with English, Portuguese, French and Spanish translations.
     */
    public enum Messages {
        NO_SESSION(
                ChatColor.RED + "You are not in a build session. Use " + ChatColor.WHITE + "/build" + ChatColor.RED + " to start one.",
                ChatColor.RED + "Você não está em uma sessão de construção. " + ChatColor.WHITE + "Use /construir" + ChatColor.RED + " para iniciar uma.",
                ChatColor.RED + "Vous n'êtes pas dans une session de construction. Utilisez " + ChatColor.WHITE + "/construire" + ChatColor.RED + " pour en commencer une.",
                ChatColor.RED + "No estás en una sesión de construcción. Usa " + ChatColor.WHITE + "/construir" + ChatColor.RED + " para comenzar una."
        ),
        SESSION_ENDED(
                ChatColor.DARK_AQUA + "Your session has ended!",
                ChatColor.DARK_AQUA + "Sua sessão foi encerrada!",
                ChatColor.DARK_AQUA + "Votre session est terminée !",
                ChatColor.DARK_AQUA + "Tu sesión ha finalizado."
        ),
        MUST_PROVIDE_LABEL(
                ChatColor.RED + "You must provide a theme." + ChatColor.WHITE + " Use /theme <thème>",
                ChatColor.RED + "Você deve fornecer um tema." + ChatColor.WHITE + " Use /tema <tema>",
                ChatColor.RED + "Vous devez fournir un thème." + ChatColor.WHITE + " Utilisez /theme <thème>",
                ChatColor.RED + "Debes proporcionar un tema." + ChatColor.WHITE + " Usa /tema <tema>"
        ),
        SESSION_CLEANED(
                ChatColor.DARK_AQUA + "Cleaned area!",
                ChatColor.DARK_AQUA + "Área limpa!",
                ChatColor.DARK_AQUA + "Zone nettoyée !",
                ChatColor.DARK_AQUA + "Área limpiada!"
        ),
        SUBMIT_SUCCESS(
                ChatColor.DARK_AQUA + "Build submitted!",
                ChatColor.DARK_AQUA + "Construção submetida!",
                ChatColor.DARK_AQUA + "Construction soumise !",
                ChatColor.DARK_AQUA + "Construcción enviada!"
        ),
        BUILD_A(
                ChatColor.DARK_AQUA + "Build a(n) ",
                ChatColor.DARK_AQUA + "Construa um(a) ",
                ChatColor.DARK_AQUA + "Construisez un(e) ",
                ChatColor.DARK_AQUA + "Construir un(a) "
        ),
        BUILD_A_BOSSBAR(
                "Build a(n) ",
                "Construa um(a) ",
                "Construisez un(e) ",
                "Construir un(a) "
        ),
        TUTORIAL_BOSSBAR(
                "Type /build to start building!",
                "Digite /construir para começar a construir!",
                "Tapez /construire pour commencer à construire !",
                "Escribe /construir para comenzar a construir!"
        ),
        ERROR_SAVING_SCHEMATIC(
                ChatColor.RED + "Error saving schematic.",
                ChatColor.RED + "Erro ao salvar o esquemático.",
                ChatColor.RED + "Erreur lors de l'enregistrement du schéma.",
                ChatColor.RED + "Error al guardar el esquema."
        ),
        LABEL_HELP(
                ChatColor.GOLD + "/label <label>:" + ChatColor.DARK_AQUA + " Set a new label.",
                ChatColor.GOLD + "/tema <tema>:" + ChatColor.DARK_AQUA + " Escolha um novo tema.",
                ChatColor.GOLD + "/theme <thème>:" + ChatColor.DARK_AQUA + " Changer le thème.",
                ChatColor.GOLD + "/tema <tema>:" + ChatColor.DARK_AQUA + " Establecer un nuevo tema."
        ),

        RANDOM_LABEL_HELP(
                ChatColor.GOLD + "/randomlabel:" + ChatColor.DARK_AQUA + " Get a new random label.",
                ChatColor.GOLD + "/temaaleatorio:" + ChatColor.DARK_AQUA + " Receba um novo tema aleatório.",
                ChatColor.GOLD + "/themealeatoire:" + ChatColor.DARK_AQUA + " Obtenez un nouveau thème aléatoire.",
                ChatColor.GOLD + "/temaaleatorio:" + ChatColor.DARK_AQUA + " Obtén un nueva tema aleatoria."
        ),

        END_HELP(
                ChatColor.GOLD + "/end:" + ChatColor.DARK_AQUA + " End the building session.",
                ChatColor.GOLD + "/terminar:" + ChatColor.DARK_AQUA + " Encerre a sessão de construção.",
                ChatColor.GOLD + "/terminer:" + ChatColor.DARK_AQUA + " Mettre fin à la session de construction.",
                ChatColor.GOLD + "/terminar:" + ChatColor.DARK_AQUA + " Finalizar la sesión de construcción."
        ),

        CLEAN_HELP(
                ChatColor.GOLD + "/clean:" + ChatColor.DARK_AQUA + " Clean the building area.",
                ChatColor.GOLD + "/limpar:" + ChatColor.DARK_AQUA + " Limpe a área de construção.",
                ChatColor.GOLD + "/vider:" + ChatColor.DARK_AQUA + " Vider la zone de construction.",
                ChatColor.GOLD + "/limpar:" + ChatColor.DARK_AQUA + " Limpiar el área de construcción."
        ),

        SUBMIT_HELP(
                ChatColor.GOLD + "/submit:" + ChatColor.DARK_AQUA + " Submit your build.",
                ChatColor.GOLD + "/submeter:" + ChatColor.DARK_AQUA + " Submeta sua construção.",
                ChatColor.GOLD + "/soumettre:" + ChatColor.DARK_AQUA + " Soumettez votre construction.",
                ChatColor.GOLD + "/submeter:" + ChatColor.DARK_AQUA + " Envía tu construcción."
        ),

        NEED_SPECIFY_LANGUAGE(
                ChatColor.RED + "You must specify a language. Use /language <language>",
                ChatColor.RED + "Você deve especificar um idioma. Use /idioma <idioma>",
                ChatColor.RED + "Vous devez spécifier une langue. Utilisez /language <langue>",
                ChatColor.RED + "Debes especificar un idioma. Usa /idioma <idioma>"
        ),

        LANGUAGE_SET(
                ChatColor.DARK_AQUA + "Language set to ",
                ChatColor.DARK_AQUA + "Idioma definido para ",
                ChatColor.DARK_AQUA + "Langue changée pour ",
                ChatColor.DARK_AQUA + "Idioma establecido a "
        ),

        INVALID_LANGUAGE(
                ChatColor.RED + "Invalid language. Use /language <language>",
                ChatColor.RED + "Idioma inválido. Use /idioma <idioma>",
                ChatColor.RED + "Langue invalide. Utilisez /langue <langue>",
                ChatColor.RED + "Idioma inválido. Usa /idioma <idioma>"
        ),

        WORLDEDIT_ENABLED_BUILDING_AREA(
                ChatColor.YELLOW + "WorldEdit commands are enabled in the building area!",
                ChatColor.YELLOW + "Comandos do WorldEdit estão habilitados na área de construção!",
                ChatColor.YELLOW + "Les commandes WorldEdit sont activées dans la zone de construction !",
                ChatColor.YELLOW + "¡Los comandos de WorldEdit están habilitados en el área de construcción!"
        ),
        ALREADY_IN_SESSION(
                ChatColor.RED + "You are already in a build session. Use " + ChatColor.WHITE + "/end" + ChatColor.RED + " to end it.",
                ChatColor.RED + "Você já está em uma sessão de construção. Use " + ChatColor.WHITE + "/terminar" + ChatColor.RED + " para encerrá-la.",
                ChatColor.RED + "Vous êtes déjà dans une session de construction. Utilisez " + ChatColor.WHITE + "/terminer" + ChatColor.RED + " pour la terminer.",
                ChatColor.RED + "Ya estás en una sesión de construcción. Usa " + ChatColor.WHITE + "/terminar" + ChatColor.RED + " para finalizarla."
        ),
        WELCOME(
                ChatColor.AQUA + "Welcome to " + ChatColor.WHITE + "MClassifier!",
                ChatColor.AQUA + "Bem-vindo ao MClassifier!",
                ChatColor.AQUA + "Bienvenue dans MClassifier!",
                ChatColor.AQUA + "¡Bienvenido a MClassifier!"
        ),
        LANGUAGE_HELP(
                ChatColor.AQUA + "Available languages: " + ChatColor.WHITE + "en, pt, fr, es" + ChatColor.AQUA + ". Use " + ChatColor.WHITE + "/language <language>" + ChatColor.AQUA + " to set your language.",
                ChatColor.AQUA + "Idiomas disponíveis: " + ChatColor.WHITE + "en, pt, fr, es" + ChatColor.AQUA + ". Use " + ChatColor.WHITE + "/idioma <idioma>" + ChatColor.AQUA + " para definir seu idioma.",
                ChatColor.AQUA + "Langues disponibles : " + ChatColor.WHITE + "en, pt, fr, es" + ChatColor.AQUA + ". Utilisez " + ChatColor.WHITE + "/language <langue>" + ChatColor.AQUA + " pour définir votre langue.",
                ChatColor.AQUA + "Idiomas disponibles: " + ChatColor.WHITE + "en, pt, fr, es" + ChatColor.AQUA + ". Usa " + ChatColor.WHITE + "/idioma <idioma>" + ChatColor.AQUA + " para establecer tu idioma."
        );


        private final String english;
        private final String portuguese;
        private final String french;
        private final String spanish;

        Messages(String english, String portuguese, String french, String spanish) {
            this.english = english;
            this.portuguese = portuguese;
            this.french = french;
            this.spanish = spanish;
        }

        public String getEnglish() {
            return english;
        }

        public String getPortuguese() {
            return portuguese;
        }

        public String getFrench() {
            return french;
        }

        public String getSpanish() {
            return spanish;
        }
    }

    /**
     * Enum representing different labels with English, Portuguese, French and Spanish translations.
     */
    public enum Labels {
        TREE("Tree", "Árvore", "Arbre", "Árbol"),
        HOUSE("House", "Casa", "Maison", "Casa"),
        CASTLE_TOWER("Castle Tower", "Torre de Castelo", "Tour de Château", "Torre de Castillo"),
        FARM("Farm", "Fazenda", "Ferme", "Granja"),
        TREE_HOUSE("Tree House", "Casa na Árvore", "Maison dans l'Arbre", "Casa en el Árbol"),
        LIGHTHOUSE("Lighthouse", "Farol", "Phare", "Faro"),
        EGYPTIAN_PYRAMID("Egyptian Pyramid", "Pirâmide Egípcia", "Pyramide Égyptienne", "Pirámide Egipcia"),
        CHURCH("Church", "Igreja", "Église", "Iglesia"),
        SHIP("Ship", "Navio", "Navire", "Barco");

        private final String english;
        private final String portuguese;
        private final String french;
        private final String spanish;

        Labels(String english, String portuguese, String french, String spanish) {
            this.english = english;
            this.portuguese = portuguese;
            this.french = french;
            this.spanish = spanish;
        }

        public String getEnglish() {
            return english;
        }

        public String getPortuguese() {
            return portuguese;
        }

        public String getFrench() {
            return french;
        }

        public String getSpanish() {
            return spanish;
        }

        }


    /**
     * A HashMap to store the language preferences of players.
     */
    public static final HashMap<String, String> userLanguages = loadLanguages();

    /**
     * Loads the language preferences from the YAML file.
     *
     * @return A HashMap containing player names as keys and their language preferences as values.
     */
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

    /**
     * Sets the language preference for the specified player and updates the YAML file.
     *
     * @param playerName The name of the player whose language preference is being set.
     * @param language   The language preference to set for the player.
     */
    public void setLanguage(String playerName, String language) {
        // Update the HashMap
        userLanguages.put(playerName, language);

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
        if (language.equals("pt")) {
            return message.getPortuguese();
        }
        if (language.equals("fr")) {
            return message.getFrench();
        }
        if (language.equals("es")) {
            return message.getSpanish();
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
        if (language.equals("fr")) {
            return label.getFrench();
        }
        if (language.equals("es")) {
            return label.getSpanish();
        }
        return label.getEnglish();
    }
}