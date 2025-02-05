package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.Dimension;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Level.class)
public abstract class MixinLevel implements StationFlatteningWorld {
    @Shadow public abstract Chunk getChunk(int x, int z);

    @Shadow @Final public Dimension dimension;

    @Shadow protected abstract void method_235(int i, int j, int k, int l);

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return getChunk(x, z).getBlockState(x & 15, y, z & 15);
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return getChunk(x, z).setBlockState(x & 15, y, z & 15, blockState);
    }

    @Override
    public BlockState setBlockStateWithNotify(int x, int y, int z, BlockState blockState) {
        BlockState oldBlockState = setBlockState(x, y, z, blockState);
        if (oldBlockState != null) {
            method_235(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @Override
    public BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState blockState, int meta) {
        return getChunk(x, z).setBlockStateWithMetadata(x & 0xF, y, z & 0xF, blockState, meta);
    }

    @Override
    public BlockState setBlockStateWithMetadataWithNotify(int x, int y, int z, BlockState blockState, int meta) {
        BlockState oldBlockState = setBlockStateWithMetadata(x, y, z, blockState, meta);
        if (oldBlockState != null) {
            method_235(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            index = 9
    )
    private int stationapi_changeCaveSoundY(int y) {
        return y + getBottomY();
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 10
    )
    private int stationapi_changeTickY(int y) {
        return y + getBottomY();
    }

    @Redirect(
            method = "method_248()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
                    args = "array=get"
            )
    )
    private byte stationapi_redirectTilesAccess(byte[] array, int index) {
        return 0;
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 1
            ),
            index = 11
    )
    private int stationapi_changeTickBlockId(
            int blockId,
            @Local(index = 5) Chunk chunk,
            @Local(index = 8) int x, @Local(index = 10) int y, @Local(index = 9) int z
    ) {
        return chunk.getBlockState(x, y, z).getBlock().id;
    }

    @ModifyConstant(method = {
        "getTileId",
        "isTileLoaded",
        "method_155",
        "setTileWithMetadata",
        "setTileInChunk",
        "getTileMeta",
        "method_223",
        "isAboveGround",
        "getLightLevel",
        "placeTile(IIIZ)I",
        "method_164",
        "method_205",
        "method_193",
        "method_248"
    }, constant = @Constant(intValue = 128))
    private int changeMaxHeight(int value) {
        return getTopY();
    }

    @ModifyConstant(method = {
            "getTileId",
            "setTileWithMetadata",
            "setTileInChunk",
            "getTileMeta",
            "method_223",
            "isAboveGround",
            "getLightLevel",
            "placeTile(IIIZ)I",
            "method_164",
            "method_205"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
    private int changeBottomYGE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "isTileLoaded",
            "method_155",
            "method_248",
            "method_162"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO))
    private int changeBottomYLT(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_228"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_OR_EQUAL_TO_ZERO))
    private int changeBottomYLE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_193",
            "method_164"
    }, constant = @Constant(intValue = 0, ordinal = 0))
    private int changeBottomY(int value) {
        return getBottomY();
    }

    @ModifyConstant(
            method = {
                    "getLightLevel",
                    "placeTile(IIIZ)I",
                    "method_164",
                    "method_228",
                    "method_248"
            },
            constant = @Constant(intValue = 127)
    )
    private int changeMaxBlockHeight(int value) {
        return getTopY() - 1;
    }

    @ModifyConstant(method = {
        "method_162"
    }, constant = @Constant(intValue = 200))
    private int changeMaxEntityCalcHeight(int value) {
        return getTopY() + 64;
    }

    @Unique
    @Override
    public int getHeight() {
        return ((StationDimension) this.dimension).getActualLevelHeight();
    }

    @Override
    public int getBottomY() {
        return ((StationDimension) this.dimension).getActualBottomY();
    }

    @ModifyVariable(
            method = "canPlaceTile(IIIIZI)Z",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            )
    )
    private BlockBase accountForAirBlock(BlockBase value) {
        return value == States.AIR.get().getBlock() ? null : value;
    }
}
