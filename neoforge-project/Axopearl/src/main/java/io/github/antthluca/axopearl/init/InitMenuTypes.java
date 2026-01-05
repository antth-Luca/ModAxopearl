package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.screens.EletricAnvilMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
        Registries.MENU, Axopearl.MODID);

    // Menu Types
    public static final DeferredHolder<MenuType<?>, MenuType<EletricAnvilMenu>> ELETRIC_ANVIL_MENU = MENU_TYPES.register(
        "eletric_anvil_menu", () -> new MenuType(EletricAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
