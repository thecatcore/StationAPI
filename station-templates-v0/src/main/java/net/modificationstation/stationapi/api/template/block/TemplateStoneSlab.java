package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.StoneSlab;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStoneSlab extends StoneSlab implements BlockTemplate {
    
    public TemplateStoneSlab(Identifier identifier, boolean flag) {
        this(BlockTemplate.getNextId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStoneSlab(int i, boolean flag) {
        super(i, flag);
    }
}
