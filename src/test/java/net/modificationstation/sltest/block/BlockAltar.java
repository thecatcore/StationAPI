package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.modificationstation.sltest.texture.TextureListener;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class BlockAltar extends TemplateBlockBase {

    public BlockAltar(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getTextureForSide(int side) {
        return TextureListener.altarTextures[side];
    }
}
