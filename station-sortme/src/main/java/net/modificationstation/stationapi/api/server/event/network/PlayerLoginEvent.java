package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.ServerPlayer;
import net.mine_diver.unsafeevents.Event;

@RequiredArgsConstructor
public class PlayerLoginEvent extends Event {

    public final ServerPlayer player;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}