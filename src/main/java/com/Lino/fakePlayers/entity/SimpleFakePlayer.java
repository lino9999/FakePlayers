package com.Lino.fakePlayers.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Alternative FakePlayer implementation using TabList API
 * This approach doesn't require NMS/CraftBukkit internals
 */
public class SimpleFakePlayer {
    private final String name;
    private final UUID uuid;
    private Location location;
    private long lastMessageTime = 0;

    public SimpleFakePlayer(String name, Location location) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void sendMessage(String message) {
        // Rate limiting
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMessageTime < 1000) {
            return;
        }
        lastMessageTime = currentTime;

        // Broadcast the message as if from this fake player
        String formattedMessage = "<" + name + "> " + message;
        Bukkit.broadcastMessage(formattedMessage);
    }

    public void sendActionBar(String message) {
        // Send as regular message prefixed with player name
        // Since sendActionBar requires newer API versions
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§7[" + name + "] " + message);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SimpleFakePlayer that = (SimpleFakePlayer) obj;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}