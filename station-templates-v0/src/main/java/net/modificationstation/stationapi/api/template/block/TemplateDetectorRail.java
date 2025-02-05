package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DetectorRail;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDetectorRail extends DetectorRail implements BlockTemplate {
    
    public TemplateDetectorRail(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDetectorRail(int id, int texture) {
        super(id, texture);
    }
}
