package com.Lino.fakePlayers.conversation;

import java.util.*;

public class MessageDatabase {
    private static final Map<ConversationTopic, List<String>> messages = new HashMap<>();
    private static final Map<ConversationTopic, List<String>> responses = new HashMap<>();

    static {
        messages.put(ConversationTopic.GREETING, Arrays.asList(
                "hey everyone!",
                "hello {world}!",
                "good {time} everyone",
                "whats up guys",
                "anyone online?",
                "hi all :)",
                "greetings!"
        ));

        messages.put(ConversationTopic.BUILDING, Arrays.asList(
                "working on my new house",
                "anyone have spare wood?",
                "building a castle, need stone",
                "check out my build at spawn",
                "who wants to help me build?",
                "making a farm, need ideas",
                "my base is coming along nicely"
        ));

        messages.put(ConversationTopic.MINING, Arrays.asList(
                "going mining, anyone wanna join?",
                "found diamonds at y=-59!",
                "strip mining is so boring",
                "just found a huge cave",
                "anyone seen ancient debris?",
                "need iron badly",
                "mining at level -54"
        ));

        messages.put(ConversationTopic.COMBAT, Arrays.asList(
                "anyone up for pvp?",
                "just killed the ender dragon!",
                "need help with a raid",
                "lost all my stuff :(",
                "creeper blew up my house",
                "anyone have spare armor?",
                "going to the nether"
        ));

        messages.put(ConversationTopic.TRADING, Arrays.asList(
                "selling diamonds for emeralds",
                "anyone trading books?",
                "need enchanted tools",
                "buying netherite",
                "trading post at spawn",
                "looking for mending books",
                "who has blaze rods?"
        ));

        messages.put(ConversationTopic.EXPLORING, Arrays.asList(
                "found a village at 200, -300",
                "exploring new chunks",
                "anyone seen a jungle?",
                "looking for ocean monument",
                "found a stronghold!",
                "searching for mesa biome",
                "lets explore together"
        ));

        messages.put(ConversationTopic.FARMING, Arrays.asList(
                "my wheat farm is huge now",
                "anyone have carrots?",
                "building auto farm",
                "need bonemeal",
                "villager trading hall done",
                "making a mob grinder",
                "sugar cane farm working"
        ));

        messages.put(ConversationTopic.REDSTONE, Arrays.asList(
                "redstone is confusing",
                "making auto sorter",
                "piston door broken again",
                "anyone good with redstone?",
                "my farm is fully automatic",
                "need more repeaters",
                "working on flying machine"
        ));

        messages.put(ConversationTopic.WEATHER, Arrays.asList(
                "its {time} already",
                "rain again...",
                "nice weather today",
                "thunderstorm incoming",
                "perfect mining weather",
                "sun is finally out",
                "wish it would stop raining"
        ));

        messages.put(ConversationTopic.HELP, Arrays.asList(
                "how do i craft this?",
                "need help with something",
                "can someone help me?",
                "stuck in a cave",
                "how do enchantments work?",
                "whats the best y level?",
                "tips for beginners?"
        ));

        messages.put(ConversationTopic.GENERAL, Arrays.asList(
                "this server is great",
                "{online} players online nice",
                "having fun today",
                "minecraft is awesome",
                "been playing all day",
                "time flies when mining",
                "love this game"
        ));

        responses.put(ConversationTopic.GREETING, Arrays.asList(
                "hey!",
                "hello there",
                "hi {player}!",
                "whats up",
                "hey how are you",
                "good {time}!",
                "welcome!"
        ));

        responses.put(ConversationTopic.BUILDING, Arrays.asList(
                "i have some spare",
                "your builds are cool",
                "ill help!",
                "need any blocks?",
                "sounds awesome",
                "cant wait to see it",
                "good luck!"
        ));

        responses.put(ConversationTopic.MINING, Arrays.asList(
                "ill join!",
                "be careful down there",
                "good luck",
                "found any diamonds?",
                "watch for lava",
                "bring torches",
                "happy mining!"
        ));

        responses.put(ConversationTopic.COMBAT, Arrays.asList(
                "gg!",
                "nice job",
                "rip",
                "ill help",
                "be careful",
                "good fight",
                "you got this"
        ));

        responses.put(ConversationTopic.TRADING, Arrays.asList(
                "interested!",
                "what rates?",
                "i might have some",
                "deal!",
                "sounds fair",
                "ill check",
                "good prices"
        ));

        responses.put(ConversationTopic.EXPLORING, Arrays.asList(
                "cool find!",
                "im coming",
                "nice discovery",
                "coordinates?",
                "on my way",
                "awesome!",
                "lets go"
        ));

        responses.put(ConversationTopic.FARMING, Arrays.asList(
                "nice farm",
                "impressive!",
                "can i see?",
                "need help?",
                "sounds efficient",
                "good job",
                "teach me"
        ));

        responses.put(ConversationTopic.REDSTONE, Arrays.asList(
                "i can help",
                "redstone is hard",
                "youtube it",
                "keep trying",
                "youll get it",
                "ask someone",
                "good luck"
        ));

        responses.put(ConversationTopic.WEATHER, Arrays.asList(
                "yeah true",
                "i know right",
                "agreed",
                "same here",
                "definitely",
                "for sure",
                "yep"
        ));

        responses.put(ConversationTopic.HELP, Arrays.asList(
                "sure!",
                "i can help",
                "what do you need?",
                "on my way",
                "let me help",
                "no problem",
                "happy to help"
        ));

        responses.put(ConversationTopic.GENERAL, Arrays.asList(
                "same!",
                "agreed",
                "yeah its great",
                "totally",
                "for sure",
                "absolutely",
                "same here"
        ));
    }

    public static List<String> getMessages(ConversationTopic topic) {
        return messages.getOrDefault(topic, messages.get(ConversationTopic.GENERAL));
    }

    public static List<String> getResponses(ConversationTopic topic) {
        return responses.getOrDefault(topic, responses.get(ConversationTopic.GENERAL));
    }
}