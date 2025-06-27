package com.example.fakeplayers.manager;

import com.example.fakeplayers.FakePlayers;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListManager {
    private final FakePlayers plugin;
    private int additionalPlayers = 0;

    public ServerListManager(FakePlayers plugin) {
        this.plugin = plugin;
    }

    public void setAdditionalPlayers(int count) {
        this.additionalPlayers = count;
    }

    public int getAdditionalPlayers() {
        return additionalPlayers;
    }

    public void handleServerListPing(ServerListPingEvent event) {
        int fakePlayers = plugin.getFakePlayerManager().getFakePlayerCount();
        int totalAdditional = fakePlayers + additionalPlayers;

        if (totalAdditional > 0) {
            event.setMaxPlayers(event.getMaxPlayers() + totalAdditional);
        }
    }
}