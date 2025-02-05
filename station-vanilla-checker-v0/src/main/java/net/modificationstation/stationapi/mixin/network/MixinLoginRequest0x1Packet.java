package net.modificationstation.stationapi.mixin.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.packet.login.LoginRequest0x1Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.network.VanillaChecker.MASK;

@Mixin(LoginRequest0x1Packet.class)
public class MixinLoginRequest0x1Packet {

    @Shadow public String username;

    @Shadow public long worldSeed;

    @Inject(
            method = "<init>(Ljava/lang/String;IJB)V",
            at = @At("RETURN")
    )
    @Environment(EnvType.SERVER)
    private void injectStAPIFlag(String username, int protocolVersion, long worldSeed, byte dimensionId, CallbackInfo ci) {
        this.username += MODID + ";";
    }

    @Inject(
            method = "<init>(Ljava/lang/String;I)V",
            at = @At("RETURN")
    )
    @Environment(EnvType.CLIENT)
    private void injectStAPIFlag(String username, int protocolVersion, CallbackInfo ci) {
        worldSeed |= MASK;
    }
}
