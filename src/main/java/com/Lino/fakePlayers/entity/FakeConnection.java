package com.Lino.fakePlayers.entity;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

import javax.annotation.Nullable;
import java.net.SocketAddress;

public class FakeConnection extends Connection {

    public FakeConnection(PacketFlow flow) {
        super(flow);
        this.channel = new EmbeddedChannel();
        this.address = new SocketAddress() {
            @Override
            public String toString() {
                return "FakePlayer";
            }
        };
    }

    @Override
    public void send(Packet<?> packet) {
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketSendListener listener) {
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketSendListener listener, boolean flush) {
    }

    @Override
    public void disconnect(net.minecraft.network.chat.Component reason) {
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void setProtocol(net.minecraft.network.ConnectionProtocol protocol) {
    }

    @Override
    public void setListenerForServerboundHandshake(PacketListener listener) {
    }
}