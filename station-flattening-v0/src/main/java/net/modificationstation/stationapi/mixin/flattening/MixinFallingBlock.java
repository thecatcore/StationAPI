package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.FallingBlock;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.IsBlockReplaceableEvent;
import net.modificationstation.stationapi.api.item.AutomaticItemPlacementContext;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlock.class)
public class MixinFallingBlock {

    @Redirect(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;canPlaceTile(IIIIZI)Z"
            )
    )
    private boolean redirectCanPlace(Level world, int blockId, int x, int y, int z, boolean skipEntityCollisionCheck, int side) {
        return StationAPI.EVENT_BUS.post(IsBlockReplaceableEvent.builder().context(new AutomaticItemPlacementContext(world, new BlockPos(x, y, z), Direction.DOWN, null, Direction.byId(side))).build()).context.canPlace();
    }
}
