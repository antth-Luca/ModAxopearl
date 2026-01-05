package io.github.antthluca.axopearl.utils;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantmentUtils {
    public static int ANVIL_BASE_COST = 1;

    public record EnchantResult(int xpCost, ItemEnchantments.Mutable targetEnchs) { }

    public static EnchantResult enchantItem(Holder<Item> targetHolder, ItemEnchantments.Mutable targetEnchs, ItemEnchantments enchsToAdd) {
        int xpCost = 0;
        ItemEnchantments.Mutable returnEnchs = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        Iterator iterator = enchsToAdd.entrySet().iterator();
        while (iterator.hasNext()) {
            Object2IntMap.Entry<Holder<Enchantment>> toAddEntry = (Object2IntMap.Entry) iterator.next();
            Holder<Enchantment> toAddKey = (Holder) toAddEntry.getKey();
            if (canEnchantPerSupport(targetHolder, toAddKey)) {
                if (canEnchantPerCompatibility(targetEnchs, toAddKey)) {
                    int toAddLevel = toAddEntry.getIntValue();
                    int targetLevel = targetEnchs.getLevel(toAddKey);
                    int finalLevel = targetLevel == toAddLevel ? toAddLevel + 1 : Math.max(targetLevel, toAddLevel);
                    if (finalLevel != targetLevel) {
                        returnEnchs.set(toAddKey, finalLevel);
                        xpCost += finalLevel - 1;
                    }
                }
            }
        }
        return new EnchantResult(xpCost, returnEnchs);
    }

    public static boolean canEnchantPerCompatibility(ItemEnchantments.Mutable targetEnchs, Holder<Enchantment> toAddKey) {
        for (Holder<Enchantment> targetKey : targetEnchs.keySet()) {
            if (targetKey.equals(toAddKey)) {
                continue;
            }
            if (!Enchantment.areCompatible(toAddKey, targetKey)) {
                return false;
            }
        }

        return true;
    }

    public static boolean canEnchantPerSupport(Holder<Item> targetHolder, Holder<Enchantment> toAddHolder) {
        return ((Enchantment) toAddHolder.value()).definition().supportedItems().contains(targetHolder);
    }
}
