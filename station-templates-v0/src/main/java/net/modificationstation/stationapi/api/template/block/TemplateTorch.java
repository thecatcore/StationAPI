package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Torch;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTorch extends Torch implements BlockTemplate {
    
    public TemplateTorch(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTorch(int i, int j) {
        super(i, j);
    }
}
