package com.Lino.fakePlayers.entity;

import java.util.*;

public class NameGenerator {
    private static final List<String> prefixes = Arrays.asList(
            "Pro", "Epic", "Cool", "Dark", "Fire", "Ice", "Storm", "Shadow", "Light", "Crystal",
            "Thunder", "Dragon", "Phoenix", "Wolf", "Eagle", "Tiger", "Bear", "Fox", "Hawk", "Raven"
    );

    private static final List<String> names = Arrays.asList(
            "Player", "Gamer", "Master", "Lord", "King", "Knight", "Warrior", "Mage", "Hunter", "Ninja",
            "Samurai", "Wizard", "Champion", "Legend", "Hero", "Guardian", "Defender", "Slayer", "Reaper", "Phantom"
    );

    private static final List<String> suffixes = Arrays.asList(
            "123", "456", "789", "007", "420", "666", "777", "2000", "3000", "9000",
            "XD", "YT", "TV", "GG", "OP", "HD", "4K", "Pro", "Max", "Plus"
    );

    private static final Set<String> usedNames = new HashSet<>();
    private static final Random random = new Random();

    public static String generateRandomName() {
        String name;
        int attempts = 0;

        do {
            StringBuilder sb = new StringBuilder();

            if (random.nextBoolean()) {
                sb.append(prefixes.get(random.nextInt(prefixes.size())));
            }

            sb.append(names.get(random.nextInt(names.size())));

            if (random.nextBoolean() || attempts > 5) {
                sb.append(suffixes.get(random.nextInt(suffixes.size())));
            }

            name = sb.toString();
            attempts++;

            if (attempts > 20) {
                name = "Player" + System.currentTimeMillis() % 10000;
                break;
            }
        } while (usedNames.contains(name) || name.length() > 16);

        usedNames.add(name);
        return name;
    }

    public static void clearUsedNames() {
        usedNames.clear();
    }
}