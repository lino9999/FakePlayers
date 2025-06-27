package com.Lino.fakePlayers.manager;

import com.Lino.fakePlayers.FakePlayers;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListManager {
    private final FakePlayers plugin;
    private int additionalPlayers = 0;

    public ServerListManager(FakePlayers plugin) {
        this.plugin = plugin;
        this.additionalPlayers = plugin.getConfig().getInt("server-list.additional-players", 0);
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
            // Increase the online player count shown in server list
            event.setNumPlayers(event.getNumPlayers() + totalAdditional);
        }
    }
}