package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TallGrass;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTallGrass extends TallGrass implements BlockTemplate {
    
    public TemplateTallGrass(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTallGrass(int i, int j) {
        super(i, j);
    }
}
