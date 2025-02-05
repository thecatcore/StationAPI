package net.modificationstation.stationapi.impl.resource;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.PathUtil;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.metadata.PackResourceMetadata;
import net.modificationstation.stationapi.impl.resource.metadata.ResourceMetadataMap;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class DefaultResourcePack implements ResourcePack {

    private static final Path[] ROOT_PATHS = ModID.MINECRAFT.getContainer().getRootPaths().toArray(new Path[0]);
    private static final Map<ResourceType, List<Path>> NAMESPACE_PATHS = Util.make(new EnumMap<>(ResourceType.class), m -> {
        for (ResourceType type : ResourceType.values())
            for (Path rootPath : ROOT_PATHS) {
                Path typePath = rootPath.resolve(type.getDirectory());
                if (Files.exists(typePath))
                    m.computeIfAbsent(type, key -> new ReferenceArrayList<>()).add(typePath);
            }
    });

    private static final PackResourceMetadata METADATA = new PackResourceMetadata("fixText", 13);
    private static final ResourceMetadataMap METADATA_MAP = ResourceMetadataMap.of(PackResourceMetadata.SERIALIZER, METADATA);

    @Override
    public @Nullable InputSupplier<InputStream> openRoot(String... segments) {
        PathUtil.validatePath(segments);
        List<String> list = List.of(segments);
        for (Path path : ROOT_PATHS) {
            Path path2 = PathUtil.getPath(path, list);
            if (!Files.exists(path2) || !DirectoryResourcePack.isValidPath(path2)) continue;
            return InputSupplier.create(path2);
        }
        return null;
    }

    @Override
    public @Nullable InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return PathUtil.split(id.id).get().map(segments -> {
            String string = id.modID.toString();
            if (NAMESPACE_PATHS.containsKey(type)) for (Path path : NAMESPACE_PATHS.get(type)) {
                Path path2 = PathUtil.getPath(path.resolve(string), segments);
                if (!Files.exists(path2) || !DirectoryResourcePack.isValidPath(path2)) continue;
                return InputSupplier.create(path2);
            }
            return null;
        }, result -> {
            LOGGER.error("Invalid path {}: {}", id, result.message());
            return null;
        });
    }

    @Override
    public void findResources(ResourceType type, ModID namespace, String prefix, ResultConsumer consumer) {
        PathUtil.split(prefix).get().ifLeft(segments -> {
            if (NAMESPACE_PATHS.containsKey(type)) {
                List<Path> list = NAMESPACE_PATHS.get(type);
                int i = list.size();
                if (i == 1) DefaultResourcePack.collectIdentifiers(consumer, namespace, list.get(0), segments);
                else if (i > 1) {
                    HashMap<Identifier, InputSupplier<InputStream>> map = new HashMap<>();
                    for (int j = 0; j < i - 1; ++j)
                        DefaultResourcePack.collectIdentifiers(map::putIfAbsent, namespace, list.get(j), segments);
                    Path path = list.get(i - 1);
                    if (map.isEmpty()) DefaultResourcePack.collectIdentifiers(consumer, namespace, path, segments);
                    else {
                        DefaultResourcePack.collectIdentifiers(map::putIfAbsent, namespace, path, segments);
                        map.forEach(consumer);
                    }
                }
            }
        }).ifRight(result -> LOGGER.error("Invalid path {}: {}", prefix, result.message()));
    }

    private static void collectIdentifiers(ResourcePack.ResultConsumer consumer, ModID namespace, Path root, List<String> prefixSegments) {
        Path path = root.resolve(namespace.toString());
        DirectoryResourcePack.findResources(namespace, path, prefixSegments, consumer);
    }

    @Override
    public Set<ModID> getNamespaces(ResourceType type) {
        return Set.of(ModID.MINECRAFT);
    }

    @Override
    public <T> @Nullable T parseMetadata(ResourceMetadataReader<T> metaReader) {
        InputSupplier<InputStream> inputSupplier = this.openRoot("pack.mcmeta");
        if (inputSupplier == null) return METADATA_MAP.get(metaReader);
        try (InputStream inputStream = inputSupplier.get()){
            T object = AbstractFileResourcePack.parseMetadata(metaReader, inputStream);
            if (object == null) return METADATA_MAP.get(metaReader);
            return object;
        } catch (IOException iOException) {
            // empty catch block
        }
        return METADATA_MAP.get(metaReader);
    }

    @Override
    public String getName() {
        return "vanilla";
    }

    @Override
    public boolean isAlwaysStable() {
        return true;
    }

    @Override
    public void close() {}
}
