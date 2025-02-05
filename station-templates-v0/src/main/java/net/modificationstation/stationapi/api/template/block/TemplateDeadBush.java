package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DeadBush;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDeadBush extends DeadBush implements BlockTemplate {

    public TemplateDeadBush(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDeadBush(int i, int j) {
        super(i, j);
    }
}
