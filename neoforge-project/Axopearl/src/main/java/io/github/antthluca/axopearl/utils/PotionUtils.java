package io.github.antthluca.axopearl.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class PotionUtils {
    public static Potion getPotion(ItemStack stack) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        if (contents == PotionContents.EMPTY) { return null; }
        return getPotion(contents);
    }

    public static Potion getPotion(PotionContents contents) {
        Holder<Potion> holder = contents.potion().orElse(null);
        return holder == null ? null : holder.value();
    }
}
