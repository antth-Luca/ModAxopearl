package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.items.EnchantedTechPearlItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Items
        // Runes
    public static final DeferredItem<Item> IRON_NODE = ITEMS.registerItem(
        "iron_node", Item::new);

    public static final DeferredItem<Item> AIR_RUNE = ITEMS.registerItem(
        "air_rune", Item::new);

    public static final DeferredItem<Item> EARTH_RUNE = ITEMS.registerItem(
        "earth_rune", Item::new);

    public static final DeferredItem<Item> FIRE_RUNE = ITEMS.registerItem(
        "fire_rune", Item::new);
    
    public static final DeferredItem<Item> WATER_RUNE = ITEMS.registerItem(
        "water_rune", Item::new);
    
        // Components
    public static final DeferredItem<Item> AXO_CAPACITOR = ITEMS.registerItem(
        "axo_capacitor", Item::new);

    public static final DeferredItem<Item> AXO_CORE = ITEMS.registerItem(
        "axo_core", Item::new);

    public static final DeferredItem<Item> BEACON_COMPONENT = ITEMS.registerItem(
        "beacon_component", Item::new);

    public static final DeferredItem<Item> FILTER_COMPONENT = ITEMS.registerItem(
        "filter_component", Item::new);

    public static final DeferredItem<Item> FOOD_SUPPORT_COMPONENT = ITEMS.registerItem(
        "food_support_component", Item::new);

    public static final DeferredItem<Item> O2_SUPPORT_COMPONENT = ITEMS.registerItem(
        "o2_support_component", Item::new);

    public static final DeferredItem<Item> POTION_SUPPORT_COMPONENT = ITEMS.registerItem(
        "potion_support_component", Item::new);

    public static final DeferredItem<Item> REPAIR_COMPONENT = ITEMS.registerItem(
        "repair_component", Item::new);

    public static final DeferredItem<Item> UNDERWATER_THRUSTER_COMPONENT = ITEMS.registerItem(
        "underwater_thruster_component", Item::new);

        // Pearls
    public static final DeferredItem<Item> SHELL_SHARD = ITEMS.registerItem(
        "shell_shard", Item::new);

    public static final DeferredItem<Item> AXOLOTL_GOO = ITEMS.registerItem(
        "axolotl_goo", Item::new);
    
    public static final DeferredItem<Item> CALCIUM_PASTE = ITEMS.registerItem(
        "calcium_paste", Item::new);

    public static final DeferredItem<Item> CALCIUM_SHELL = ITEMS.registerItem(
        "calcium_shell", Item::new);
    
    public static final DeferredItem<Item> WHITE_TECHPEARL = ITEMS.registerItem(
        "white_techpearl", Item::new);

    public static final DeferredItem<Item> ENCHANTED_TECHPEARL = ITEMS.register(
        "enchanted_techpearl", resLocId -> new EnchantedTechPearlItem(resLocId));

    public static final DeferredItem<Item> TECHPEARL_OF_INFINITY = ITEMS.registerItem(
        "techpearl_of_infinity", Item::new);

    public static final DeferredItem<Item> TECHPEARL_OF_METABOLISM = ITEMS.registerItem(
        "techpearl_of_metabolism", Item::new);

    public static final DeferredItem<Item> TECHPEARL_OF_MOBILITY = ITEMS.registerItem(
        "techpearl_of_mobility", Item::new);

    public static final DeferredItem<Item> TECHPEARL_OF_PURITY = ITEMS.registerItem(
        "techpearl_of_purity", Item::new);

    public static final DeferredItem<Item> AXOPEARL = ITEMS.registerItem(
        "axopearl", Item::new);
}
