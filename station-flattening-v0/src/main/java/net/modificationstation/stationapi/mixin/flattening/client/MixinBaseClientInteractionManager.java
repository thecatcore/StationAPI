package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BaseClientInteractionManager.class)
public class MixinBaseClientInteractionManager {
	@Shadow @Final protected Minecraft minecraft;
	
	@ModifyConstant(method = "method_1716", constant = @Constant(intValue = 256))
	private int changeMetaShift(int value) {
		return 268435456;
	}
}
