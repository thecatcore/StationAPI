package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.impl.client.render.StationTextureManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TextureManager.class)
public class MixinTextureManager implements StationTextureManagerImpl.StationTextureManagerAccess {

    @Unique
    private final StationTextureManagerImpl stationapi$stationTextureManagerImpl = new StationTextureManagerImpl((TextureManager) (Object) this);

    @Override
    @Unique
    public StationTextureManagerImpl stationapi$stationTextureManager() {
        return stationapi$stationTextureManagerImpl;
    }
}
