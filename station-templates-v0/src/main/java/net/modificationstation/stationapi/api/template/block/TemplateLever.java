package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Lever;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLever extends Lever implements BlockTemplate {

    public TemplateLever(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLever(int i, int j) {
        super(i, j);
    }
}
