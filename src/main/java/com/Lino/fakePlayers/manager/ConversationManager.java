package com.Lino.fakePlayers.manager;

import com.example.fakeplayers.FakePlayers;
import com.example.fakeplayers.conversation.ConversationTopic;
import com.example.fakeplayers.conversation.MessageDatabase;
import com.example.fakeplayers.entity.FakePlayer;
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
        conversationTask = Bukkit.getScheduler().runTaskTimer(plugin, this::processConversations, 100L, 60L);
    }

    public void stopConversations() {
        if (conversationTask != null) {
            conversationTask.cancel();
            conversationTask = null;
        }
    }

    private void processConversations() {
        List<FakePlayer> players = new ArrayList<>(plugin.getFakePlayerManager().getAllFakePlayers());
        if (players.size() < 2) return;

        long currentTime = System.currentTimeMillis();

        for (FakePlayer player : players) {
            String name = player.getName();
            Long lastTime = lastMessageTime.get(name);

            if (lastTime == null || currentTime - lastTime > 5000 + random.nextInt(10000)) {
                if (random.nextDouble() < 0.3) {
                    generateConversation(player, players);
                    lastMessageTime.put(name, currentTime);
                }
            }
        }

        activeTopics.entrySet().removeIf(entry -> currentTime - entry.getValue().getStartTime() > 60000);
    }

    private void generateConversation(FakePlayer initiator, List<FakePlayer> allPlayers) {
        ConversationTopic topic = activeTopics.get(initiator.getName());

        if (topic == null || topic.isExpired()) {
            topic = ConversationTopic.randomTopic();
            activeTopics.put(initiator.getName(), topic);
        }

        String message = generateMessage(initiator, topic);

        if (random.nextDouble() < 0.4 && allPlayers.size() > 1) {
            FakePlayer responder = selectResponder(initiator, allPlayers);
            if (responder != null) {
                message = "@" + responder.getName() + " " + message;
                activeTopics.put(responder.getName(), topic);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    String response = generateResponse(responder, topic, initiator);
                    responder.sendMessage(response);
                }, 40L + random.nextInt(60));
            }
        }

        initiator.sendMessage(message);
    }

    private String generateMessage(FakePlayer player, ConversationTopic topic) {
        List<String> messages = MessageDatabase.getMessages(topic);
        String template = messages.get(random.nextInt(messages.size()));
        return personalizeMessage(template, player);
    }

    private String generateResponse(FakePlayer responder, ConversationTopic topic, FakePlayer initiator) {
        List<String> responses = MessageDatabase.getResponses(topic);
        String template = responses.get(random.nextInt(responses.size()));
        template = "@" + initiator.getName() + " " + template;
        return personalizeMessage(template, responder);
    }

    private String personalizeMessage(String template, FakePlayer player) {
        template = template.replace("{player}", player.getName());
        template = template.replace("{time}", getTimeOfDay());
        template = template.replace("{world}", player.getBukkitEntity().getWorld().getName());
        template = template.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()));
        return template;
    }

    private FakePlayer selectResponder(FakePlayer initiator, List<FakePlayer> players) {
        List<FakePlayer> candidates = new ArrayList<>(players);
        candidates.remove(initiator);
        if (candidates.isEmpty()) return null;
        return candidates.get(random.nextInt(candidates.size()));
    }

    private String getTimeOfDay() {
        long time = Bukkit.getWorlds().get(0).getTime();
        if (time < 6000) return "morning";
        else if (time < 12000) return "day";
        else if (time < 18000) return "evening";
        else return "night";
    }
}