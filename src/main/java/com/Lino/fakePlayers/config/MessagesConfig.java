package com.Lino.fakePlayers.config;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.conversation.ConversationTopic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class MessagesConfig {
    private final FakePlayers plugin;
    private File configFile;
    private FileConfiguration config;
    private final Map<ConversationTopic, List<String>> messages = new HashMap<>();
    private final Map<ConversationTopic, List<String>> responses = new HashMap<>();

    public MessagesConfig(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Load default config from resources
        InputStream defConfigStream = plugin.getResource("messages.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream)
            );
            config.setDefaults(defConfig);
        }

        loadMessages();
    }

    private void loadMessages() {
        messages.clear();
        responses.clear();

        // Load messages for each topic
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String topicName : messagesSection.getKeys(false)) {
                try {
                    ConversationTopic topic = ConversationTopic.valueOf(topicName.toUpperCase());
                    List<String> topicMessages = messagesSection.getStringList(topicName);
                    if (!topicMessages.isEmpty()) {
                        messages.put(topic, topicMessages);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Unknown conversation topic in messages.yml: " + topicName);
                }
            }
        }

        // Load responses for each topic
        ConfigurationSection responsesSection = config.getConfigurationSection("responses");
        if (responsesSection != null) {
            for (String topicName : responsesSection.getKeys(false)) {
                try {
                    ConversationTopic topic = ConversationTopic.valueOf(topicName.toUpperCase());
                    List<String> topicResponses = responsesSection.getStringList(topicName);
                    if (!topicResponses.isEmpty()) {
                        responses.put(topic, topicResponses);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Unknown conversation topic in messages.yml: " + topicName);
                }
            }
        }
    }

    public List<String> getMessages(ConversationTopic topic) {
        return messages.getOrDefault(topic, getDefaultMessages(topic));
    }

    public List<String> getResponses(ConversationTopic topic) {
        return responses.getOrDefault(topic, getDefaultResponses(topic));
    }

    private List<String> getDefaultMessages(ConversationTopic topic) {
        // Return default messages if not found in config
        return Arrays.asList(
                "Hello everyone!",
                "Anyone online?",
                "What's up?"
        );
    }

    private List<String> getDefaultResponses(ConversationTopic topic) {
        // Return default responses if not found in config
        return Arrays.asList(
                "Hey!",
                "Hello there",
                "Hi!"
        );
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml!");
            e.printStackTrace();
        }
    }

    public void addMessage(ConversationTopic topic, String message) {
        List<String> topicMessages = messages.computeIfAbsent(topic, k -> new ArrayList<>());
        topicMessages.add(message);

        List<String> configMessages = config.getStringList("messages." + topic.name().toLowerCase());
        configMessages.add(message);
        config.set("messages." + topic.name().toLowerCase(), configMessages);

        saveConfig();
    }

    public void addResponse(ConversationTopic topic, String response) {
        List<String> topicResponses = responses.computeIfAbsent(topic, k -> new ArrayList<>());
        topicResponses.add(response);

        List<String> configResponses = config.getStringList("responses." + topic.name().toLowerCase());
        configResponses.add(response);
        config.set("responses." + topic.name().toLowerCase(), configResponses);

        saveConfig();
    }
}