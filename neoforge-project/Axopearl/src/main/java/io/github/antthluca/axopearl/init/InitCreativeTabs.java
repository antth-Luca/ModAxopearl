package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB, Axopearl.MODID
    );

    // Tabs
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register(
        "main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.axopearl"))
            .icon(() -> new ItemStack(InitItems.AXOPEARL.get()))
            .displayItems((displayParams, output) -> {
                InitItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            }).build()
    );
}
