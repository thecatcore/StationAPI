package net.modificationstation.stationapi.impl.server.gui.screen.container;

import net.minecraft.container.ContainerBase;
import net.minecraft.container.ContainerListener;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;
import net.modificationstation.stationapi.mixin.container.server.ServerPlayerAccessor;

public class GuiHelperServerImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message) {
        message.objects = new Object[] { null };
        ((ServerPlayerAccessor) player).invokeMethod_314();
        message.ints = new int[] { ((ServerPlayerAccessor) player).getField_260() };
    }

    @Override
    protected void afterPacketSent(PlayerBase player, ContainerBase container) {
        player.container = container;
        container.currentContainerId = ((ServerPlayerAccessor) player).getField_260();
        container.addListener((ContainerListener) player);
    }
}
