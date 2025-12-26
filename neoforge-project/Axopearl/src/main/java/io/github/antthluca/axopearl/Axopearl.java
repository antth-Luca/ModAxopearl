package io.github.antthluca.axopearl;

import io.github.antthluca.axopearl.init.InitCreativeTabs;
import io.github.antthluca.axopearl.init.InitItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.ModContainer;

@Mod(Axopearl.MODID)
public class Axopearl {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "axopearl";

    public Axopearl(IEventBus bus, ModContainer container) {
        // Init
        InitItems.ITEMS.register(bus);

        //InitCreativeTabs.TABS.register(bus);

        NeoForge.EVENT_BUS.register(this);

        // Register the item to a vanilla creative tab
        bus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // TODO: Adicionar;
        // }

        // if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // TODO: Adicionar;
        // }
    }
}
