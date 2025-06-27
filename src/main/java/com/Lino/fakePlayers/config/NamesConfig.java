package com.Lino.fakePlayers.config;

import com.Lino.fakePlayers.FakePlayers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class NamesConfig {
    private final FakePlayers plugin;
    private File configFile;
    private FileConfiguration config;
    private List<String> prefixes = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> suffixes = new ArrayList<>();

    public NamesConfig(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "namePlayers.yml");

        if (!configFile.exists()) {
            plugin.saveResource("namePlayers.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Load default config from resources
        InputStream defConfigStream = plugin.getResource("namePlayers.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream)
            );
            config.setDefaults(defConfig);
        }

        loadNames();
    }

    private void loadNames() {
        prefixes = config.getStringList("prefixes");
        names = config.getStringList("names");
        suffixes = config.getStringList("suffixes");

        // Ensure we have some names
        if (prefixes.isEmpty()) {
            prefixes = getDefaultPrefixes();
        }
        if (names.isEmpty()) {
            names = getDefaultNames();
        }
        if (suffixes.isEmpty()) {
            suffixes = getDefaultSuffixes();
        }
    }

    public List<String> getPrefixes() {
        return new ArrayList<>(prefixes);
    }

    public List<String> getNames() {
        return new ArrayList<>(names);
    }

    public List<String> getSuffixes() {
        return new ArrayList<>(suffixes);
    }

    private List<String> getDefaultPrefixes() {
        return Arrays.asList(
                "Pro", "Epic", "Cool", "Dark", "Fire", "Ice", "Storm", "Shadow", "Light", "Crystal",
                "Thunder", "Dragon", "Phoenix", "Wolf", "Eagle", "Tiger", "Bear", "Fox", "Hawk", "Raven",
                "Ultra", "Mega", "Super", "Hyper", "Alpha", "Beta", "Omega", "Prime", "Elite", "Master"
        );
    }

    private List<String> getDefaultNames() {
        return Arrays.asList(
                "Player", "Gamer", "Master", "Lord", "King", "Knight", "Warrior", "Mage", "Hunter", "Ninja",
                "Samurai", "Wizard", "Champion", "Legend", "Hero", "Guardian", "Defender", "Slayer", "Reaper", "Phantom",
                "Sniper", "Assassin", "Ranger", "Paladin", "Berserker", "Archer", "Swordsman", "Fighter", "Crusher", "Destroyer"
        );
    }

    private List<String> getDefaultSuffixes() {
        return Arrays.asList(
                "123", "456", "789", "007", "420", "666", "777", "2000", "3000", "9000",
                "XD", "YT", "TV", "GG", "OP", "HD", "4K", "Pro", "Max", "Plus",
                "Jr", "Sr", "III", "IV", "V", "2024", "2025", "X", "Z", "Q"
        );
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save namePlayers.yml!");
            e.printStackTrace();
        }
    }

    public void addPrefix(String prefix) {
        if (!prefixes.contains(prefix)) {
            prefixes.add(prefix);
            config.set("prefixes", prefixes);
            saveConfig();
        }
    }

    public void addName(String name) {
        if (!names.contains(name)) {
            names.add(name);
            config.set("names", names);
            saveConfig();
        }
    }

    public void addSuffix(String suffix) {
        if (!suffixes.contains(suffix)) {
            suffixes.add(suffix);
            config.set("suffixes", suffixes);
            saveConfig();
        }
    }
}