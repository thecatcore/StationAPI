package net.modificationstation.sltest.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

@HasMetaNamedBlockItem
public class ModdedMetaBlock extends TemplateBlockBase {

    public ModdedMetaBlock(Identifier id, Material material) {
        super(id, material);
    }

    @Override
    protected int droppedMeta(int i) {
        return i;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getBrightness(BlockView arg, int i, int j, int k) {
        if (arg.getTileMeta(i, j, k) == 1) {
            lightchecks = 6;
            return 1.0F;
        }
        if (lightchecks != 0) {
            lightchecks--;
            return 1.0F;
        }
        else
            return super.getBrightness(arg, i, j, k);
    }

    public int lightchecks = 0;

//    @Override
//    public float getHardness(int i) {
//        return i == 1 ? 0.5F : getHardness();
//    }

//    @Override
//    public boolean isEffectiveFor(ToolLevel toolLevel, int i) {
//        return i == 1 && toolLevel instanceof Hoe && toolLevel.getToolLevel() < 1;
//    }
}
