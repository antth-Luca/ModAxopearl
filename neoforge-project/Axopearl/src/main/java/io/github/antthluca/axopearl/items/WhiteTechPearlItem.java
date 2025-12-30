package io.github.antthluca.axopearl.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WhiteTechPearlItem extends Item {
    public WhiteTechPearlItem(ResourceLocation id) {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))
            .enchantable(2)
        );
    }
}
