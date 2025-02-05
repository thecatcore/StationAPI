package net.modificationstation.stationapi.api.mod.entrypoint;

import net.mine_diver.unsafeevents.listener.EventListener;

import java.lang.annotation.*;

/**
 * Lets the enrtypoint class change some setup behaviour.
 * @author mine_diver
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entrypoint {
    /**
     * Allows entrypoint to change {@link EventListener} registration behaviour.
     * @return the {@link EventBusPolicy} to be applied to the current entrypoint.
     */
    EventBusPolicy eventBus() default @EventBusPolicy;

    /**
     * Marks the field to be set to the entrypoint's instance.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {}

    /**
     * Marks the field to be set to the specified modid instance.
     * Empty modid defaults to entrypoint's modid.
     * @see net.modificationstation.stationapi.api.registry.ModID
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ModID {
        /**
         * @return requested modid. Empty defaults to entrypoint's modid.
         */
        String value() default "";
    }

    /**
     * Marks the field to be set to a logger with the specified name.
     * If name is left empty, it defaults to "modid|Mod".
     * @see org.apache.logging.log4j.Logger
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Logger {
        /**
         * @return logger's name. Empty defaults to "modid|Mod"
         */
        String value() default "";
    }
}
