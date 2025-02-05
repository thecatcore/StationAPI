package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicTessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tessellator.class)
public class MixinTessellator {

    @Unique
    private final ArsenicTessellator arsenic_plugin = new ArsenicTessellator((Tessellator) (Object) this);

    @Inject(
            method = "addVertex(DDD)V",
            at = @At("RETURN")
    )
    private void afterVertex(double e, double f, double par3, CallbackInfo ci) {
        arsenic_plugin.afterVertex();
    }
}
