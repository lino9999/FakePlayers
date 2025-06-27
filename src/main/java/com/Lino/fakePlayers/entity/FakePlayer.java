package com.Lino.fakePlayers.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;

import java.util.UUID;

public class FakePlayer extends ServerPlayer {
    private final String skin;
    private final String signature;

    public FakePlayer(MinecraftServer server, ServerLevel world, GameProfile profile, ClientInformation clientInfo, String skin, String signature) {
        super(server, world, profile, clientInfo);
        this.skin = skin;
        this.signature = signature;

        if (skin != null && signature != null) {
            profile.getProperties().put("textures", new Property("textures", skin, signature));
        }
    }

    public static FakePlayer create(String name, Location location) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        ClientInformation clientInfo = ClientInformation.createDefault();

        String[] skinData = SkinDatabase.getRandomSkin();
        FakePlayer fakePlayer = new FakePlayer(server, world, profile, clientInfo, skinData[0], skinData[1]);

        Connection connection = new FakeConnection(PacketFlow.SERVERBOUND);
        ServerGamePacketListenerImpl packetListener = new ServerGamePacketListenerImpl(
                server,
                connection,
                fakePlayer,
                CommonListenerCookie.createInitial(profile, false)
        );

        fakePlayer.connection = packetListener;
        fakePlayer.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        return fakePlayer;
    }

    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }

    public void sendMessage(String message) {
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("FakePlayers"), () -> {
            getBukkitEntity().chat(message);
        });
    }
}