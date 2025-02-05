package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.Fire;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Fire.class)
public interface FireAccessor {

    @Accessor
    int[] getField_2307();

    @Accessor
    void setField_2307(int[] field_2307);

    @Accessor
    int[] getSpreadDelayChance();

    @Accessor
    void setSpreadDelayChance(int[] spreadDelayChance);
}
