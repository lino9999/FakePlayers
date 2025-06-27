package com.Lino.fakePlayers.listener;

import com.Lino.fakePlayers.FakePlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListListener implements Listener {
    private final FakePlayers plugin;

    public ServerListListener(FakePlayers plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent event) {
        plugin.getServerListManager().handleServerListPing(event);
    }
}