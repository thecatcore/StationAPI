package net.modificationstation.stationapi.api.client.color.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface BlockColorProvider {
   int getColor(BlockState state, @Nullable BlockView world, @Nullable TilePos pos, int tintIndex);
}
