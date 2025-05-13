package com.trustwallet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class to manage configuration properties.
 */
public class ConfigManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final String DEFAULT_CONFIG_PATH = "src/main/java/com/trustwallet/config/config.properties";
    private static final String ENV_CONFIG_PATH_TEMPLATE = "src/main/java/com/trustwallet/config/environments/%s.properties";

    private static ConfigManager instance;
    private final Properties properties;

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfigManager() {
        properties = new Properties();
        loadDefaultConfig();
    }

    /**
     * Get singleton instance.
     *
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Load default configuration.
     */
    private void loadDefaultConfig() {
        try (InputStream input = new FileInputStream(DEFAULT_CONFIG_PATH)) {
            properties.load(input);
            log.info("Loaded default configuration from: {}", DEFAULT_CONFIG_PATH);
        } catch (IOException e) {
            log.error("Failed to load default configuration", e);
        }
    }

    /**
     * Load environment-specific configuration.
     *
     * @param environment environment name (dev, staging, prod)
     */
    public void loadConfig(String environment) {
        String configPath = String.format(ENV_CONFIG_PATH_TEMPLATE, environment);
        try (InputStream input = new FileInputStream(configPath)) {
            // Clear existing properties and load default first
            properties.clear();
            loadDefaultConfig();

            // Then load environment-specific properties
            properties.load(input);
            log.info("Loaded {} environment configuration from: {}", environment, configPath);
        } catch (IOException e) {
            log.error("Failed to load {} environment configuration", environment, e);
        }
    }

    /**
     * Get property value.
     *
     * @param key property key
     * @return property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with default.
     *
     * @param key          property key
     * @param defaultValue default value if property not found
     * @return property value or default value if not found
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get integer property value.
     *
     * @param key property key
     * @return integer property value or 0 if not found or not an integer
     */
    public int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key, "0"));
        } catch (NumberFormatException e) {
            log.error("Failed to parse integer property: {}", key, e);
            return 0;
        }
    }

    /**
     * Get boolean property value.
     *
     * @param key property key
     * @return boolean property value or false if not found or not a boolean
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key, "false"));
    }
}