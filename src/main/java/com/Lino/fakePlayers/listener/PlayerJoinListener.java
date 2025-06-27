package com.Lino.fakePlayers.listener;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.entity.SimpleFakePlayer;
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

        // Don't greet fake players
        if (plugin.getFakePlayerManager().isFakePlayer(player.getName())) {
            return;
        }

        double greetingChance = plugin.getConfig().getDouble("conversation.greeting-chance", 0.3);

        if (random.nextDouble() < greetingChance && plugin.getFakePlayerManager().getFakePlayerCount() > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                SimpleFakePlayer fakePlayer = plugin.getFakePlayerManager().getRandomFakePlayer();
                if (fakePlayer != null) {
                    String[] greetings = {
                            "welcome " + player.getName() + "!",
                            "hey " + player.getName(),
                            "hi " + player.getName() + "!",
                            player.getName() + " is here!",
                            "wb " + player.getName(),
                            "hello " + player.getName() + "!",
                            "good to see you " + player.getName()
                    };
                    fakePlayer.sendMessage(greetings[random.nextInt(greetings.length)]);
                }
            }, 40L + random.nextInt(60));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Don't say goodbye to fake players
        if (plugin.getFakePlayerManager().isFakePlayer(player.getName())) {
            return;
        }

        double farewellChance = plugin.getConfig().getDouble("conversation.farewell-chance", 0.2);

        if (random.nextDouble() < farewellChance && plugin.getFakePlayerManager().getFakePlayerCount() > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                SimpleFakePlayer fakePlayer = plugin.getFakePlayerManager().getRandomFakePlayer();
                if (fakePlayer != null) {
                    String[] farewells = {
                            "bye " + player.getName(),
                            "cya " + player.getName(),
                            "see you later",
                            player.getName() + " left :(",
                            "goodbye " + player.getName(),
                            "later " + player.getName()
                    };
                    fakePlayer.sendMessage(farewells[random.nextInt(farewells.length)]);
                }
            }, 20L + random.nextInt(40));
        }
    }
}