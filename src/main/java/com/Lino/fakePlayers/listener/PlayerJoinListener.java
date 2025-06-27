package com.Lino.fakePlayers.listener;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.entity.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerJoinListener implements Listener {
    private final FakePlayers plugin;
    private final Random random = new Random();

    public PlayerJoinListener(FakePlayers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getFakePlayerManager().isFakePlayer(player)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (random.nextDouble() < 0.3 && !plugin.getFakePlayerManager().getAllFakePlayers().isEmpty()) {
                    FakePlayer fakePlayer = getRandomFakePlayer();
                    if (fakePlayer != null) {
                        String[] greetings = {
                                "welcome " + player.getName() + "!",
                                "hey " + player.getName(),
                                "hi " + player.getName() + "!",
                                player.getName() + " is here!",
                                "wb " + player.getName()
                        };
                        fakePlayer.sendMessage(greetings[random.nextInt(greetings.length)]);
                    }
                }
            }, 40L + random.nextInt(60));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getFakePlayerManager().isFakePlayer(player)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (random.nextDouble() < 0.2 && !plugin.getFakePlayerManager().getAllFakePlayers().isEmpty()) {
                    FakePlayer fakePlayer = getRandomFakePlayer();
                    if (fakePlayer != null) {
                        String[] farewells = {
                                "bye " + player.getName(),
                                "cya " + player.getName(),
                                "see you later",
                                player.getName() + " left :("
                        };
                        fakePlayer.sendMessage(farewells[random.nextInt(farewells.length)]);
                    }
                }
            }, 20L);
        }
    }

    private FakePlayer getRandomFakePlayer() {
        var fakePlayers = plugin.getFakePlayerManager().getAllFakePlayers();
        if (fakePlayers.isEmpty()) return null;
        return fakePlayers.stream()
                .skip(random.nextInt(fakePlayers.size()))
                .findFirst()
                .orElse(null);
    }
}