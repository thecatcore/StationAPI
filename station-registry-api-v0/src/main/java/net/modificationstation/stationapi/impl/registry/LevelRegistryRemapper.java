package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.legacy.LevelLegacyRegistry;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class LevelRegistryRemapper {

    @EventListener
    private static void saveProperties(LevelPropertiesEvent.Save event) {
        CompoundTag registriesTag = new CompoundTag();
        LevelLegacyRegistry.saveAll(registriesTag);
        event.tag.put(of(MODID, "level_serial_registries").toString(), registriesTag);
    }

    @EventListener
    private static void loadProperties(LevelPropertiesEvent.LoadOnLevelInit event) {
        String lsr = of(MODID, "level_serial_registries").toString();
        if (event.tag.containsKey(lsr))
            LevelLegacyRegistry.loadAll(event.tag.getCompoundTag(lsr));
    }
}
