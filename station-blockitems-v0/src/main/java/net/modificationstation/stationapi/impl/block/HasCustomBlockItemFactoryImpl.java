package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.*;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.IntFunction;

/**
 * {@link HasCustomBlockItemFactory} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryEvent
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see HasMetaNamedBlockItem
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class HasCustomBlockItemFactoryImpl {

    /**
     * Processes {@link HasCustomBlockItemFactory} annotation if present via {@link BlockItemFactoryEvent} hook.
     * @param event blockitemfactory callback.
     */
    @EventListener
    private static void getBlockItemFactory(BlockItemFactoryEvent event) {
        if (event.block instanceof CustomBlockItemFactoryProvider provider)
            event.currentFactory = provider.getBlockItemFactory();
        Class<? extends BlockBase> blockClass = event.block.getClass();
        if (blockClass.isAnnotationPresent(HasCustomBlockItemFactory.class)) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                //noinspection unchecked
                event.currentFactory = (IntFunction<Block>) LambdaMetafactory.metafactory(
                        lookup,
                        "apply",
                        MethodType.methodType(IntFunction.class),
                        MethodType.methodType(Object.class, int.class),
                        lookup.findConstructor(blockClass.getAnnotation(HasCustomBlockItemFactory.class).value(), MethodType.methodType(void.class, int.class)),
                        MethodType.methodType(Block.class, int.class)
                ).getTarget().invokeExact();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
