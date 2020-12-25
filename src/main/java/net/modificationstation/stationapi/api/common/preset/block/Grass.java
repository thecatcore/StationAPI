package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Grass extends net.minecraft.block.Grass {

    public Grass(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Grass(int id) {
        super(id);
    }

    @Override
    public Grass disableNotifyOnMetaDataChange() {
        return (Grass) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Grass sounds(BlockSounds sounds) {
        return (Grass) super.sounds(sounds);
    }

    @Override
    public Grass setLightOpacity(int i) {
        return (Grass) super.setLightOpacity(i);
    }

    @Override
    public Grass setLightEmittance(float f) {
        return (Grass) super.setLightEmittance(f);
    }

    @Override
    public Grass setBlastResistance(float resistance) {
        return (Grass) super.setBlastResistance(resistance);
    }

    @Override
    public Grass setHardness(float hardness) {
        return (Grass) super.setHardness(hardness);
    }

    @Override
    public Grass setUnbreakable() {
        return (Grass) super.setUnbreakable();
    }

    @Override
    public Grass setTicksRandomly(boolean ticksRandomly) {
        return (Grass) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Grass setName(String string) {
        return (Grass) super.setName(string);
    }

    @Override
    public Grass disableStat() {
        return (Grass) super.disableStat();
    }
}