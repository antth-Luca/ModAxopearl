package io.github.antthluca.axopearl.init;

import java.util.function.Supplier;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB, Axopearl.MODID
    );

    // Tabs
    public static final Supplier<CreativeModeTab> MAIN = TABS.register(
        "main", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.axopearl"))
            .icon(() -> InitItems.AXOPEARL.toStack())
            .displayItems((displayParams, output) -> {
                InitItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            }).build()
    );
}
