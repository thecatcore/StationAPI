package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow private Level level;
	
	@Shadow private Minecraft client;

	@SuppressWarnings("MixinAnnotationTarget")
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
	private int changeMinHeight(int value) {
		return level.getBottomY();
	}

	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 0, ordinal = 5))
	private int changeMinBlockHeight(int value) {
		return level.getBottomY();
	}

	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return level.getTopY();
	}
	
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 127))
	private int changeMaxBlockHeight(int value) {
		return level.getTopY() - 1;
	}
	
	@ModifyConstant(method = "method_1537()V", constant = @Constant(intValue = 8))
	private int changeSectionCount(int value) {
		return level == null ? value : level.countVerticalSections();
	}
	
	@ModifyConstant(method = "playLevelEvent", constant = @Constant(intValue = 0xFF, ordinal = 0))
	private int changeBlockIDBitmask1(int value) {
		return 0x0FFFFFFF;
	}
	
	@ModifyConstant(method = "playLevelEvent", constant = @Constant(intValue = 0xFF, ordinal = 1))
	private int changeBlockIDBitmask2(int value) {
		return 0x0FFFFFFF;
	}
	
	@ModifyConstant(method = "playLevelEvent", constant = @Constant(intValue = 0xFF, ordinal = 2))
	private int changeMetaBitmask(int value) {
		return 15;
	}
	
	@ModifyConstant(method = "playLevelEvent", constant = @Constant(intValue = 8))
	private int changeMetaBitshift(int value) {
		return 28;
	}

	@ModifyArg(
			method = "method_1537",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_66;<init>(Lnet/minecraft/level/Level;Ljava/util/List;IIIII)V"
			),
			index = 3
	)
	private int offsetYBlockCoord1(int original) {
		return level == null ? original : level.getBottomY() + original;
	}

	@ModifyArg(
			method = "method_1553(III)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_66;method_298(III)V"
			),
			index = 1
	)
	private int offsetYBlockCoord2(int y) {
		return level.getBottomY() + y;
	}

	@ModifyVariable(
			method = "method_1543(IIIIII)V",
			at = @At("HEAD"),
			index = 2,
			argsOnly = true
	)
	private int modWhateverTheFuckThisIs1(int value) {
		return value - level.getBottomY();
	}

	@ModifyVariable(
			method = "method_1543(IIIIII)V",
			at = @At("HEAD"),
			index = 5,
			argsOnly = true
	)
	private int modWhateverTheFuckThisIs2(int value) {
		return value - level.getBottomY();
	}
}
