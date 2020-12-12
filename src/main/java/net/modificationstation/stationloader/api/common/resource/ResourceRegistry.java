package net.modificationstation.stationloader.api.common.resource;

import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.Registry;

public class ResourceRegistry extends Registry<Resource> {

    public ResourceRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public Resource getByIdentifier(Identifier identifier) {
        Resource resource = super.getByIdentifier(identifier);
        if (resource == null) {
            resource = new Resource(identifier, getClass().getResource(String.format("assets/%s/%s", identifier.getModID(), identifier.getId())));
            registerValue(identifier, resource);
        }
        return resource;
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}