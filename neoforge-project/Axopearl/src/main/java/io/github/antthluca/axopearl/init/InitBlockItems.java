package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.items.AxolotlShelterItemBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Block Items
    public static final DeferredItem<Item> AXOLOTL_SHELTER = BLOCK_ITEMS.register(
        "axolotl_shelter", resLocId -> new AxolotlShelterItemBlock(resLocId));

    public static final DeferredItem<Item> ELETRIC_ANVIL = BLOCK_ITEMS.register(
        "eletric_anvil", () -> new BlockItem(InitBlocks.ELETRIC_ANVIL.get(), new Item.Properties()));
}
