package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.SkylandsLevelSource;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SkylandsLevelSource.class)
public class MixinSkylandsLevelSource {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/level/Level;[BII)Lnet/minecraft/level/chunk/Chunk;"
            )
    )
    private Chunk redirectChunk(Level world, byte[] tiles, int xPos, int zPos) {
        return new FlattenedChunk(world, xPos, zPos);
    }

    @Inject(
            method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/chunk/Chunk;generateHeightmap()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void populateChunk(int j, int par2, CallbackInfoReturnable<Chunk> cir, byte[] tiles, Chunk chunk) {
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
}
