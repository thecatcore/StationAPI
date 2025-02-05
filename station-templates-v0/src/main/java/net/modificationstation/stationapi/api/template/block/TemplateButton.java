package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Button;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateButton extends Button implements BlockTemplate {

    public TemplateButton(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateButton(int id, int texture) {
        super(id, texture);
    }
}
