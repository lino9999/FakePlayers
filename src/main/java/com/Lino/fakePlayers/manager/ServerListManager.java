package com.Lino.fakePlayers.manager;

import com.Lino.fakePlayers.FakePlayers;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;

public class ServerListManager {
    private final FakePlayers plugin;
    private int additionalPlayers = 0;
    private ProtocolManager protocolManager;
    private boolean protocolLibAvailable = false;

    public ServerListManager(FakePlayers plugin) {
        this.plugin = plugin;
        this.additionalPlayers = plugin.getConfig().getInt("server-list.additional-players", 0);

        // Check if ProtocolLib is available
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolLibAvailable = true;
            setupProtocolLib();
        } else {
            plugin.getLogger().warning("ProtocolLib not found! Server list modification will not work.");
        }
    }

    private void setupProtocolLib() {
        protocolManager = ProtocolLibrary.getProtocolManager();

        // Listen for server ping packets
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL,
                PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
                    return;
                }

                try {
                    PacketContainer packet = event.getPacket();
                    WrappedServerPing ping = packet.getServerPings().read(0);

                    if (ping == null) return;

                    // Cast to FakePlayers to access our methods
                    FakePlayers fp = (FakePlayers) plugin;
                    int fakePlayers = fp.getFakePlayerManager().getFakePlayerCount();
                    int totalAdditional = fakePlayers + additionalPlayers;

                    if (totalAdditional > 0) {
                        // Modify player count
                        ping.setPlayersOnline(ping.getPlayersOnline() + totalAdditional);

                        // Write back the modified ping
                        packet.getServerPings().write(0, ping);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Error modifying server list ping: " + e.getMessage());
                }
            }
        });
    }

    public void setAdditionalPlayers(int count) {
        this.additionalPlayers = count;
    }

    public int getAdditionalPlayers() {
        return additionalPlayers;
    }

    public boolean isProtocolLibAvailable() {
        return protocolLibAvailable;
    }

    public void handleServerListPing(org.bukkit.event.server.ServerListPingEvent event) {
        // This is kept for compatibility but actual modification is done via ProtocolLib
        if (!protocolLibAvailable) {
            // Without ProtocolLib, we can only modify the MOTD
            int fakePlayers = plugin.getFakePlayerManager().getFakePlayerCount();
            if (fakePlayers > 0 && plugin.getConfig().getBoolean("server-list.show-in-motd", false)) {
                String motd = event.getMotd();
                event.setMotd(motd + "\n§7Fake Players: " + fakePlayers);
            }
        }
    }

    public void shutdown() {
        if (protocolManager != null && protocolLibAvailable) {
            protocolManager.removePacketListeners(plugin);
        }
    }
}