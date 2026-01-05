package io.github.antthluca.axopearl.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantedTechPearlItem extends Item {
    public EnchantedTechPearlItem(ResourceLocation id) {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        );
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
