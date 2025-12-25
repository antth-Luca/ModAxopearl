package io.github.antthluca.axopearl;

import io.github.antthluca.axopearl.init.InitCreativeTabs;
import io.github.antthluca.axopearl.init.InitItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Axopearl.MODID)
public class Axopearl {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "axopearl";

    public Axopearl(IEventBus bus, ModContainer container) {
        // Init
        InitItems.ITEMS.register(bus);

        InitCreativeTabs.TABS.register(bus);
    }
}
