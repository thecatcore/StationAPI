package net.modificationstation.stationapi.api.template.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.block.*;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.Util;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public interface BlockTemplate<T extends BlockBase> extends CustomAtlasProvider, BlockToolLogic, BlockStateHolder, ItemConvertible, BlockItemToggle<T>, DropWithBlockState, DropListProvider {

    @Override
    default T mineableBy(Identifier toolTag, int level) {
        return Util.assertImpl();
    }

    @Override
    default List<BiTuple<Identifier, Integer>> getToolTagEffectiveness() {
        return Util.assertImpl();
    }

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((BlockBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    @Environment(EnvType.CLIENT)
    default Atlas getAtlas() {
        return Util.assertImpl();
    }

    @Override
    default StateManager<BlockBase, BlockState> getStateManager() {
        return Util.assertImpl();
    }

    @Override
    default BlockState getDefaultState() {
        return Util.assertImpl();
    }

    @Override
    default void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        Util.assertImpl();
    }

    @Override
    default void setDefaultState(BlockState state) {
        Util.assertImpl();
    }

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

    @Override
    default T disableBlockItem() {
        return Util.assertImpl();
    }

    @Override
    default boolean isBlockItemDisabled() {
        return Util.assertImpl();
    }

    @Override
    default List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    @Override
    default void dropWithChance(Level arg, int i, int j, int k, BlockState state, int l, float f) {
        Util.assertImpl();
    }
}
