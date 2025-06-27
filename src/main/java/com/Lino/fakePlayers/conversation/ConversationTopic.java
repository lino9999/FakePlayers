package com.Lino.fakePlayers.conversation;

import java.util.Random;

public enum ConversationTopic {
    GREETING,
    BUILDING,
    MINING,
    COMBAT,
    TRADING,
    EXPLORING,
    FARMING,
    REDSTONE,
    WEATHER,
    HELP,
    GENERAL;

    private static final Random random = new Random();
    private long startTime;

    public static ConversationTopic randomTopic() {
        ConversationTopic[] topics = values();
        ConversationTopic topic = topics[random.nextInt(topics.length)];
        topic.startTime = System.currentTimeMillis();
        return topic;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > 30000;
    }
}