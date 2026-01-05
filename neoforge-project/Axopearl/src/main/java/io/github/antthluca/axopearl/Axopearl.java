package io.github.antthluca.axopearl;

import io.github.antthluca.axopearl.init.InitBlockEntities;
import io.github.antthluca.axopearl.init.InitBlockItems;
import io.github.antthluca.axopearl.init.InitBlocks;
import io.github.antthluca.axopearl.init.InitCreativeTabs;
import io.github.antthluca.axopearl.init.InitDataComponentTypes;
import io.github.antthluca.axopearl.init.InitItems;
import io.github.antthluca.axopearl.init.InitLootModifiers;
import io.github.antthluca.axopearl.init.InitMobEffects;
import io.github.antthluca.axopearl.init.InitPotions;
import io.github.antthluca.axopearl.init.InitRecipeSerializers;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.ModContainer;

@Mod(Axopearl.MODID)
public class Axopearl {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "axopearl";

    public Axopearl(IEventBus bus, ModContainer container) {
        // Init
        InitItems.ITEMS.register(bus);
        InitBlocks.BLOCKS.register(bus);
        InitBlockEntities.BLOCK_ENTITIES.register(bus);
        InitBlockItems.BLOCK_ITEMS.register(bus);
        InitMobEffects.MOB_EFFECTS.register(bus);
        InitPotions.POTION_ITEMS.register(bus);
        InitDataComponentTypes.DATA_COMPONENTS.register(bus);
        InitCreativeTabs.TABS.register(bus);
        InitRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        InitLootModifiers.LOOT_MODIFIERS.register(bus);
        // Register the item to a vanilla creative tab
        bus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // Items
                // Runes
            event.accept(InitItems.IRON_NODE);
            event.accept(InitItems.AIR_RUNE);
            event.accept(InitItems.EARTH_RUNE);
            event.accept(InitItems.FIRE_RUNE);
            event.accept(InitItems.WATER_RUNE);
                // Components
            event.accept(InitItems.AXO_CAPACITOR);
            event.accept(InitItems.AXO_CORE);
            event.accept(InitItems.BEACON_COMPONENT);
            event.accept(InitItems.FILTER_COMPONENT);
            event.accept(InitItems.FOOD_SUPPORT_COMPONENT);
            event.accept(InitItems.O2_SUPPORT_COMPONENT);
            event.accept(InitItems.POTION_SUPPORT_COMPONENT);
            event.accept(InitItems.REPAIR_COMPONENT);
            event.accept(InitItems.UNDERWATER_THRUSTER_COMPONENT);
                // Pearls
            event.accept(InitItems.SHELL_SHARD);
            event.accept(InitItems.AXOLOTL_GOO);
            event.accept(InitItems.CALCIUM_PASTE);
            event.accept(InitItems.CALCIUM_SHELL);
            event.accept(InitItems.WHITE_TECHPEARL);
            event.accept(InitItems.ENCHANTED_TECHPEARL);
            event.accept(InitItems.TECHPEARL_OF_INFINITY);
            event.accept(InitItems.TECHPEARL_OF_METABOLISM);
            event.accept(InitItems.TECHPEARL_OF_MOBILITY);
            event.accept(InitItems.TECHPEARL_OF_PURITY);
            event.accept(InitItems.AXOPEARL);
        }

        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            // Block Items
            event.accept(InitBlockItems.AXOLOTL_SHELTER);
            event.accept(InitBlockItems.ELETRIC_ANVIL);
        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            // Potion Items
            event.accept(InitPotions.POTION_OF_AXOLOTL_BURST_OF_ENERGY);
        }
    }
}
