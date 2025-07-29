package net.gcae.utils.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for managing HTML templates
 * Provides centralized template loading and variable replacement functionality
 */
public class TemplateManager {
    
    private static final Logger logger = LoggerFactory.getLogger(TemplateManager.class);
    private final Properties templates;
    
    /**
     * Constructor that loads templates from properties file
     */
    public TemplateManager() throws IOException {
        this.templates = loadTemplates();
    }
    
    /**
     * Constructor that accepts custom template properties
     */
    public TemplateManager(Properties customTemplates) {
        this.templates = customTemplates;
    }
    
    /**
     * Loads HTML templates from properties file
     */
    private Properties loadTemplates() throws IOException {
        Properties templateProps = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("html-templates.properties")) {
            if (inputStream == null) {
                throw new IOException("Cannot find html-templates.properties in classpath");
            }
            templateProps.load(inputStream);
            logger.info("Loaded {} HTML templates from properties file", templateProps.size());
        }
        return templateProps;
    }
    
    /**
     * Gets template by key
     */
    public String getTemplate(String key) {
        String template = templates.getProperty(key);
        if (template == null) {
            logger.error("Template not found for key: {}", key);
            return "";
        }
        return template;
    }
    
    /**
     * Gets template and replaces variables in one call
     */
    public String getTemplate(String key, String... keyValuePairs) {
        String template = getTemplate(key);
        return replaceVariables(template, keyValuePairs);
    }
    
    /**
     * Replaces variables in template string
     * Variables are in the format ${variableName}
     * 
     * @param template The template string containing variables
     * @param keyValuePairs Alternating key-value pairs for replacement
     * @return Template with variables replaced
     */
    public String replaceVariables(String template, String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even number of arguments");
        }
        
        String result = template;
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            result = result.replace("${" + key + "}", value != null ? value : "");
        }
        
        return result;
    }
    
    /**
     * Checks if a template exists
     */
    public boolean hasTemplate(String key) {
        return templates.containsKey(key);
    }
    
    /**
     * Gets all available template keys
     */
    public java.util.Set<String> getTemplateKeys() {
        return templates.stringPropertyNames();
    }
    
    /**
     * Validates that all required templates are present
     */
    public boolean validateRequiredTemplates(String... requiredKeys) {
        for (String key : requiredKeys) {
            if (!hasTemplate(key)) {
                logger.error("Required template missing: {}", key);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets template with HTML escaping for variables
     */
    public String getTemplateWithEscaping(String key, String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even number of arguments");
        }
        
        // Escape HTML in values
        String[] escapedPairs = new String[keyValuePairs.length];
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            escapedPairs[i] = keyValuePairs[i]; // Key (no escaping)
            escapedPairs[i + 1] = escapeHtml(keyValuePairs[i + 1]); // Value (escaped)
        }
        
        return getTemplate(key, escapedPairs);
    }
    
    /**
     * Escapes HTML special characters
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}