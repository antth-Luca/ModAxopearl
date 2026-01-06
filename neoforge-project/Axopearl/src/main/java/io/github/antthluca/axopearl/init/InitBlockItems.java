package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.items.AxolotlShelterItemBlock;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Block Items
    public static final DeferredItem<BlockItem> AXOLOTL_SHELTER = BLOCK_ITEMS.register(
        "axolotl_shelter", resLocId -> new AxolotlShelterItemBlock(resLocId));

    public static final DeferredItem<BlockItem> ELETRIC_ANVIL = BLOCK_ITEMS.registerSimpleBlockItem(
        "eletric_anvil", InitBlocks.ELETRIC_ANVIL);
}
