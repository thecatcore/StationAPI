package net.modificationstation.stationapi.impl.client.arsenic;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.ArsenicRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder.*;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class Arsenic {

    @Entrypoint.Logger("Arsenic")
    public static final Logger LOGGER = Null.get();

    @EventListener
    private static void init(InitEvent event) {
        if (ArsenicMixinConfigPlugin.shouldApplyArsenic()) {
            LOGGER.info("Registering Arsenic renderer!");

            if (ArsenicMixinConfigPlugin.shouldForceCompatibility()) {
                LOGGER.info("Compatibility mode enabled.");
            }

            RendererAccess.INSTANCE.registerRenderer(ArsenicRenderer.INSTANCE);
            StationAPI.EVENT_BUS.register(Listener.<TextureRegisterEvent>simple()
                    .listener(Arsenic::registerTextures)
                    .build());
            StationAPI.EVENT_BUS.register(Listener.<TexturePackLoadedEvent.Before>simple()
                    .listener(Arsenic::beforeTexturePackApplied)
                    .build());
        } else {
            LOGGER.info("Different rendering plugin detected; not applying Arsenic.");
        }
    }

    private static void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrain = Atlases.getTerrain();
        ExpandableAtlas guiItems = Atlases.getGuiItems();
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FLOWING_WATER.texture), ArsenicStillWater::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FLOWING_WATER.texture + 1), ArsenicFlowingWater::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FLOWING_LAVA.texture), ArsenicStillLava::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FLOWING_LAVA.texture + 1), ArsenicFlowingLava::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FIRE.texture), ArsenicFire::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.FIRE.texture + 16), ArsenicFire::new);
        terrain.addTextureBinder(terrain.getTexture(BlockBase.PORTAL.texture), ArsenicPortal::new);
        guiItems.addTextureBinder(guiItems.getTexture(ItemBase.compass.getTexturePosition(0)), ArsenicCompass::new);
        guiItems.addTextureBinder(guiItems.getTexture(ItemBase.clock.getTexturePosition(0)), ArsenicClock::new);
    }

    private static void beforeTexturePackApplied(TexturePackLoadedEvent.Before event) {
        Map<String, Integer> textureMap = ((TextureManagerAccessor) event.textureManager).getTextures();
        new HashMap<>(textureMap).keySet().stream().filter(s -> {
            try (InputStream textureStream = event.newTexturePack.getResourceAsStream(s)) {
                return textureStream == null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).forEach(s -> GL11.glDeleteTextures(textureMap.remove(s)));
        ((TextureManagerAccessor) event.textureManager).getTextureBinders().clear();
    }
}
