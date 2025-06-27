package com.Lino.fakePlayers.manager;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.conversation.ConversationTopic;
import com.Lino.fakePlayers.entity.SimpleFakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ConversationManager {
    private final FakePlayers plugin;
    private final Random random = new Random();
    private BukkitTask conversationTask;
    private final Map<String, Long> lastMessageTime = new HashMap<>();
    private final Map<String, ConversationTopic> activeTopics = new HashMap<>();

    public ConversationManager(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public void startConversations() {
        if (!plugin.getConfig().getBoolean("conversation.enabled", true)) {
            return;
        }

        int minDelay = plugin.getConfig().getInt("conversation.min-delay-seconds", 5) * 20;
        int maxDelay = plugin.getConfig().getInt("conversation.max-delay-seconds", 15) * 20;

        conversationTask = Bukkit.getScheduler().runTaskTimer(plugin, this::processConversations, 100L,
                minDelay + random.nextInt(Math.max(1, maxDelay - minDelay)));
    }

    public void stopConversations() {
        if (conversationTask != null) {
            conversationTask.cancel();
            conversationTask = null;
        }
        lastMessageTime.clear();
        activeTopics.clear();
    }

    private void processConversations() {
        List<SimpleFakePlayer> players = new ArrayList<>(plugin.getFakePlayerManager().getAllFakePlayers());
        if (players.isEmpty()) return;

        long currentTime = System.currentTimeMillis();

        // Clean up expired topics
        activeTopics.entrySet().removeIf(entry -> {
            ConversationTopic topic = entry.getValue();
            return topic.isExpired();
        });

        // Select a random fake player to send a message
        SimpleFakePlayer speaker = players.get(random.nextInt(players.size()));
        String speakerName = speaker.getName();

        // Check cooldown
        Long lastTime = lastMessageTime.get(speakerName);
        if (lastTime != null && currentTime - lastTime < 3000) {
            return;
        }

        // Determine conversation type
        double responseChance = plugin.getConfig().getDouble("conversation.response-chance", 0.4);

        if (random.nextDouble() < responseChance && players.size() > 1) {
            // Response to another player
            generateResponse(speaker, players);
        } else {
            // New conversation starter
            generateNewMessage(speaker);
        }

        lastMessageTime.put(speakerName, currentTime);
    }

    private void generateNewMessage(SimpleFakePlayer speaker) {
        ConversationTopic topic = activeTopics.get(speaker.getName());

        if (topic == null || topic.isExpired()) {
            topic = ConversationTopic.randomTopic();
            activeTopics.put(speaker.getName(), topic);
        }

        List<String> messages = plugin.getMessagesConfig().getMessages(topic);
        if (messages.isEmpty()) return;

        String message = messages.get(random.nextInt(messages.size()));
        message = personalizeMessage(message, speaker);

        speaker.sendMessage(message);
    }

    private void generateResponse(SimpleFakePlayer responder, List<SimpleFakePlayer> allPlayers) {
        // Find a player to respond to
        SimpleFakePlayer target = selectTarget(responder, allPlayers);
        if (target == null) return;

        ConversationTopic topic = activeTopics.get(target.getName());
        if (topic == null) {
            topic = ConversationTopic.randomTopic();
        }

        activeTopics.put(responder.getName(), topic);

        List<String> responses = plugin.getMessagesConfig().getResponses(topic);
        if (responses.isEmpty()) return;

        String response = responses.get(random.nextInt(responses.size()));

        // Add mention
        if (random.nextBoolean()) {
            response = "@" + target.getName() + " " + response;
        }

        response = personalizeMessage(response, responder);

        // Delay the response slightly
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            responder.sendMessage(response);
        }, 20L + random.nextInt(40));
    }

    private SimpleFakePlayer selectTarget(SimpleFakePlayer speaker, List<SimpleFakePlayer> players) {
        List<SimpleFakePlayer> candidates = new ArrayList<>(players);
        candidates.remove(speaker);

        if (candidates.isEmpty()) return null;

        // Prefer players who recently spoke
        candidates.sort((a, b) -> {
            Long timeA = lastMessageTime.getOrDefault(a.getName(), 0L);
            Long timeB = lastMessageTime.getOrDefault(b.getName(), 0L);
            return timeB.compareTo(timeA);
        });

        // Higher chance to respond to recent speakers
        if (random.nextDouble() < 0.7 && !candidates.isEmpty()) {
            return candidates.get(0);
        }

        return candidates.get(random.nextInt(candidates.size()));
    }

    private String personalizeMessage(String template, SimpleFakePlayer player) {
        template = template.replace("{player}", player.getName());
        template = template.replace("{time}", getTimeOfDay());
        template = template.replace("{world}", player.getLocation().getWorld().getName());
        template = template.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size() + plugin.getFakePlayerManager().getFakePlayerCount()));
        template = template.replace("{x}", String.valueOf((int) player.getLocation().getX()));
        template = template.replace("{y}", String.valueOf((int) player.getLocation().getY()));
        template = template.replace("{z}", String.valueOf((int) player.getLocation().getZ()));

        return template;
    }

    private String getTimeOfDay() {
        long time = Bukkit.getWorlds().get(0).getTime();
        if (time < 6000) return "morning";
        else if (time < 12000) return "day";
        else if (time < 18000) return "evening";
        else return "night";
    }

    public void triggerConversation(SimpleFakePlayer player) {
        generateNewMessage(player);
        lastMessageTime.put(player.getName(), System.currentTimeMillis());
    }
}