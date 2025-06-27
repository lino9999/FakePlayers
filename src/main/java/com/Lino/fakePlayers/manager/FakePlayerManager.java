package com.Lino.fakePlayers.manager;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.entity.FakePlayer;
import com.Lino.fakePlayers.entity.NameGenerator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakePlayerManager {
    private final FakePlayers plugin;
    private final Map<String, FakePlayer> fakePlayers = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public FakePlayerManager(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public FakePlayer createFakePlayer(String name) {
        if (fakePlayers.containsKey(name)) {
            return null;
        }

        Location spawnLocation = getRandomSpawnLocation();
        FakePlayer fakePlayer = FakePlayer.create(name, spawnLocation);

        PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
        playerList.placeNewPlayer(fakePlayer.connection, fakePlayer, CommonListenerCookie.createInitial(fakePlayer.getGameProfile(), false));

        fakePlayers.put(name, fakePlayer);

        Bukkit.getScheduler().runTask(plugin, () -> {
            PlayerJoinEvent event = new PlayerJoinEvent(fakePlayer.getBukkitEntity(), "");
            Bukkit.getPluginManager().callEvent(event);
        });

        return fakePlayer;
    }

    public void removeFakePlayer(String name) {
        FakePlayer fakePlayer = fakePlayers.remove(name);
        if (fakePlayer != null) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                fakePlayer.connection.disconnect(net.minecraft.network.chat.Component.literal("Disconnected"));
            });
        }
    }

    public void removeAllFakePlayers() {
        List<String> names = new ArrayList<>(fakePlayers.keySet());
        for (String name : names) {
            removeFakePlayer(name);
        }
    }

    public FakePlayer getFakePlayer(String name) {
        return fakePlayers.get(name);
    }

    public Collection<FakePlayer> getAllFakePlayers() {
        return Collections.unmodifiableCollection(fakePlayers.values());
    }

    public boolean isFakePlayer(Player player) {
        return fakePlayers.containsKey(player.getName());
    }

    public int getFakePlayerCount() {
        return fakePlayers.size();
    }

    public void createRandomFakePlayers(int count) {
        for (int i = 0; i < count; i++) {
            String name = NameGenerator.generateRandomName();
            createFakePlayer(name);
        }
    }

    private Location getRandomSpawnLocation() {
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        double x = spawn.getX() + (random.nextDouble() * 20 - 10);
        double z = spawn.getZ() + (random.nextDouble() * 20 - 10);
        double y = spawn.getWorld().getHighestBlockYAt((int) x, (int) z) + 1;
        return new Location(spawn.getWorld(), x, y, z);
    }
}