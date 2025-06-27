package com.Lino.fakePlayers.entity;

import com.Lino.fakePlayers.FakePlayers;
import java.util.*;

public class NameGenerator {
    private static final Set<String> usedNames = new HashSet<>();
    private static final Random random = new Random();

    public static String generateRandomName() {
        FakePlayers plugin = FakePlayers.getInstance();
        if (plugin == null || plugin.getNamesConfig() == null) {
            return generateFallbackName();
        }

        List<String> prefixes = plugin.getNamesConfig().getPrefixes();
        List<String> names = plugin.getNamesConfig().getNames();
        List<String> suffixes = plugin.getNamesConfig().getSuffixes();

        String generatedName;
        int attempts = 0;

        do {
            StringBuilder sb = new StringBuilder();

            // Randomly add a prefix (50% chance)
            if (random.nextBoolean() && !prefixes.isEmpty()) {
                sb.append(prefixes.get(random.nextInt(prefixes.size())));
            }

            // Always add a main name
            if (!names.isEmpty()) {
                sb.append(names.get(random.nextInt(names.size())));
            } else {
                sb.append("Player");
            }

            // Randomly add a suffix (50% chance, or always after 5 attempts to ensure uniqueness)
            if ((random.nextBoolean() || attempts > 5) && !suffixes.isEmpty()) {
                sb.append(suffixes.get(random.nextInt(suffixes.size())));
            }

            generatedName = sb.toString();
            attempts++;

            // After 20 attempts, just append a timestamp to ensure uniqueness
            if (attempts > 20) {
                generatedName = "Player" + System.currentTimeMillis() % 10000;
                break;
            }
        } while (usedNames.contains(generatedName) || generatedName.length() > 16);

        usedNames.add(generatedName);
        return generatedName;
    }

    private static String generateFallbackName() {
        String name;
        do {
            name = "Player" + random.nextInt(10000);
        } while (usedNames.contains(name));

        usedNames.add(name);
        return name;
    }

    public static void clearUsedNames() {
        usedNames.clear();
    }

    public static boolean isNameUsed(String name) {
        return usedNames.contains(name);
    }

    public static void addUsedName(String name) {
        usedNames.add(name);
    }
}