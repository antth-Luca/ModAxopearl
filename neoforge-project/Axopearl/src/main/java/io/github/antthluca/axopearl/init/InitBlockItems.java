package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.items.AxolotlShelterItemBlock;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Block Items
    public static final DeferredItem<Item> AXOLOTL_SHELTER = BLOCK_ITEMS.register(
        "axolotl_shelter", AxolotlShelterItemBlock::new);
}
