package net.modificationstation.stationapi.api.server.event.network;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.login.LoginRequest0x1Packet;

@SuperBuilder
public class PlayerLoginEvent extends Event {
    public final LoginRequest0x1Packet loginPacket;
    public final ServerPlayer player;
}
