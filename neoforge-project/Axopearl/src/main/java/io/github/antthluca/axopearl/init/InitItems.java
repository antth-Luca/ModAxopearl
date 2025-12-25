package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Items
    public static final DeferredHolder<Item, Item> AIR_RUNE = ITEMS.register(
        "air_rune", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> AXO_CAPACITOR = ITEMS.register(
        "axo_capacitor", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> AXO_CORE = ITEMS.register(
        "axo_core", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> AXOLOTL_GOO = ITEMS.register(
        "axolotl_goo", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> AXOPEARL = ITEMS.register(
        "axopearl", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BEACON_COMPONENT = ITEMS.register(
        "beacon_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CALCIUM_PASTE = ITEMS.register(
        "calcium_paste", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CALCIUM_SHELL = ITEMS.register(
        "calcium_shell", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> EARTH_RUNE = ITEMS.register(
        "earth_rune", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FILTER_COMPONENT = ITEMS.register(
        "filter_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FIRE_RUNE = ITEMS.register(
        "fire_rune", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FOOD_SUPPORT_COMPONENT = ITEMS.register(
        "food_component_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> IRON_NODE = ITEMS.register(
        "iron_node", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> O2_SUPPORT_COMPONENT = ITEMS.register(
        "o2_support_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> POTION_SUPPORT_COMPONENT = ITEMS.register(
        "potion_support_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> REPAIR_COMPONENT = ITEMS.register(
        "repair_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SHELL_SHARD = ITEMS.register(
        "shell_shard", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> TECHPEARL_OF_INFINITY = ITEMS.register(
        "techpearl_of_infinity", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> TECHPEARL_OF_METABOLISM = ITEMS.register(
        "techpearl_of_infinity", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> TECHPEARL_OF_MOBILITY = ITEMS.register(
        "techpearl_of_mobility", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> TECHPEARL_OF_PURITY = ITEMS.register(
        "techpearl_of_purity", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> UNDERWATER_THRUSTER_COMPONENT = ITEMS.register(
        "underwater_thruster_component", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> WATER_RUNE = ITEMS.register(
        "water_rune", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> WHITE_TECHPEARL = ITEMS.register(
        "white_techpearl", () -> new Item(new Item.Properties()));
}
