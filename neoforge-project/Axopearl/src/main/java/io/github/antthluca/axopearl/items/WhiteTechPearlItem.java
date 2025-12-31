package io.github.antthluca.axopearl.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class WhiteTechPearlItem extends Item {
    public WhiteTechPearlItem(ResourceLocation id) {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))
            .enchantable(2)
            .component(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        );
    }
}
