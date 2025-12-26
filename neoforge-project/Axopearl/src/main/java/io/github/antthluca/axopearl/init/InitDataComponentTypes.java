package io.github.antthluca.axopearl.init;

import java.util.function.Supplier;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.data_components.Axolotls;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitDataComponentTypes {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(
        Registries.DATA_COMPONENT_TYPE, Axopearl.MODID);

        // Data Component Types
        public static final Supplier<DataComponentType<Axolotls>> AXOLOTLS = DATA_COMPONENTS.registerComponentType(
            "axolotls", builder -> builder
                .persistent(Axolotls.CODEC)
                .networkSynchronized(Axolotls.STREAM_CODEC)
                .cacheEncoding()
        );
}
