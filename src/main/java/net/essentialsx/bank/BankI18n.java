package net.essentialsx.bank;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class BankI18n {

    private final JavaPlugin plugin;
    private ResourceBundle defaultBundle;
    private ResourceBundle currentBundle;

    public BankI18n(JavaPlugin plugin, String localeStr) {
        this.plugin = plugin;
        loadBundles(localeStr);
    }

    private void loadBundles(String localeStr) {
        // Guardar archivos por defecto
        saveDefaultMessages("en");
        saveDefaultMessages("es");

        // Cargar por defecto (inglés)
        defaultBundle = loadBundle("en");

        // Cargar el locale configurado (ej: es, en)
        if (localeStr == null || localeStr.isEmpty()) {
            currentBundle = defaultBundle;
        } else {
            currentBundle = loadBundle(localeStr);
            if (currentBundle == null) {
                currentBundle = defaultBundle;
            }
        }
    }

    private void saveDefaultMessages(String lang) {
        String filename = "messages_" + lang + ".properties";
        File file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            if (plugin.getResource(filename) != null) {
                plugin.saveResource(filename, false);
            }
        }
    }

    private ResourceBundle loadBundle(String lang) {
        File file = new File(plugin.getDataFolder(), "messages_" + lang + ".properties");
        if (!file.exists()) {
            return null;
        }
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return new PropertyResourceBundle(reader);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error loading messages_" + lang + ".properties", e);
            return null;
        }
    }

    public String tl(String key, Object... args) {
        String message = getMessage(key);
        if (message == null) {
            return "<Missing translation: " + key + ">";
        }
        
        // Reemplazar color codes de essentials (&c)
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (args != null && args.length > 0) {
            try {
                return MessageFormat.format(message, args);
            } catch (IllegalArgumentException e) {
                return message; // Format exception, return raw
            }
        }
        return message;
    }

    private String getMessage(String key) {
        try {
            if (currentBundle != null && currentBundle.containsKey(key)) {
                return currentBundle.getString(key);
            }
            if (defaultBundle != null && defaultBundle.containsKey(key)) {
                return defaultBundle.getString(key);
            }
        } catch (MissingResourceException e) {
            // ignore
        }
        return null;
    }
}
