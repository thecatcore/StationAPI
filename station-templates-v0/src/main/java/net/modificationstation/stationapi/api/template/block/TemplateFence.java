package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Fence;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFence extends Fence implements BlockTemplate {

    public TemplateFence(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFence(int i, int j) {
        super(i, j);
    }
}
