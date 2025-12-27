package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Block Items
    public static final DeferredItem<Item> AXOLOTL_SHELTER = BLOCK_ITEMS.register(
        "axolotl_shelter", () -> new BlockItem(
            InitBlocks.AXOLOTL_SHELTER.get(),
            new Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, "axolotl_shelter")))));
}
