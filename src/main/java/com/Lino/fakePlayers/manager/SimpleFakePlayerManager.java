package com.Lino.fakePlayers.manager;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.entity.SimpleFakePlayer;
import com.Lino.fakePlayers.entity.NameGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleFakePlayerManager {
    private final FakePlayers plugin;
    private final Map<String, SimpleFakePlayer> fakePlayers = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public SimpleFakePlayerManager(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public SimpleFakePlayer createFakePlayer(String name) {
        if (fakePlayers.containsKey(name.toLowerCase())) {
            return null;
        }

        Location spawnLocation = getRandomSpawnLocation();
        SimpleFakePlayer fakePlayer = new SimpleFakePlayer(name, spawnLocation);

        fakePlayers.put(name.toLowerCase(), fakePlayer);

        // Announce join
        Bukkit.broadcastMessage("§e" + name + " joined the game");

        return fakePlayer;
    }

    public void removeFakePlayer(String name) {
        SimpleFakePlayer fakePlayer = fakePlayers.remove(name.toLowerCase());
        if (fakePlayer != null) {
            // Announce leave
            Bukkit.broadcastMessage("§e" + fakePlayer.getName() + " left the game");
        }
    }

    public void removeAllFakePlayers() {
        List<String> names = new ArrayList<>(fakePlayers.keySet());
        for (String name : names) {
            removeFakePlayer(name);
        }
        NameGenerator.clearUsedNames();
    }

    public SimpleFakePlayer getFakePlayer(String name) {
        return fakePlayers.get(name.toLowerCase());
    }

    public Collection<SimpleFakePlayer> getAllFakePlayers() {
        return Collections.unmodifiableCollection(fakePlayers.values());
    }

    public boolean isFakePlayer(String name) {
        return fakePlayers.containsKey(name.toLowerCase());
    }

    public boolean isFakePlayer(Player player) {
        return false; // Real players are never fake players
    }

    public int getFakePlayerCount() {
        return fakePlayers.size();
    }

    public void createRandomFakePlayers(int count) {
        int created = 0;
        for (int i = 0; i < count; i++) {
            String name = NameGenerator.generateRandomName();
            if (createFakePlayer(name) != null) {
                created++;
            }
        }

        if (created > 0) {
            plugin.getLogger().info("Created " + created + " fake players");
        }
    }

    private Location getRandomSpawnLocation() {
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        int radius = plugin.getConfig().getInt("fake-players.spawn-radius", 20);

        double x = spawn.getX() + (random.nextDouble() * radius * 2 - radius);
        double z = spawn.getZ() + (random.nextDouble() * radius * 2 - radius);
        double y = spawn.getWorld().getHighestBlockYAt((int) x, (int) z) + 1;

        return new Location(spawn.getWorld(), x, y, z);
    }

    public SimpleFakePlayer getRandomFakePlayer() {
        List<SimpleFakePlayer> players = new ArrayList<>(fakePlayers.values());
        if (players.isEmpty()) return null;
        return players.get(random.nextInt(players.size()));
    }
}