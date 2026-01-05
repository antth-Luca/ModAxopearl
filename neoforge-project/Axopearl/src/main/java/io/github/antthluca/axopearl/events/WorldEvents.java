package io.github.antthluca.axopearl.events;

import io.github.antthluca.axopearl.init.InitItems;
import io.github.antthluca.axopearl.utils.EnchantmentUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.player.AnvilCraftEvent;

public class WorldEvents {
    private static ItemStack returnItem;
    private static boolean refundXp;

    public static void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        returnItem = null;
        refundXp = false;

        ItemStack rightItemStack = event.getRight();
        Item rightItem = rightItemStack.getItem();
        ItemStack leftItemStack = event.getLeft();
        Item leftItem = leftItemStack.getItem();

        Item compWhitePearl = InitItems.WHITE_TECHPEARL.value();
        
        boolean changed = false;
        EnchantmentUtils.EnchantResult finalResult = null;
        ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(leftItemStack);
        ItemEnchantments.Mutable leftEnchantsMutable = new ItemEnchantments.Mutable(leftEnchants);
        ItemEnchantments rightEnchants = EnchantmentHelper.getEnchantmentsForCrafting(rightItemStack);

        if (leftItem == compWhitePearl) {
            if (rightItemStack.isEnchanted()) {
                if (rightItem == compWhitePearl) {
                    // Enchant, but no XP cost
                    changed = true;
                    finalResult = EnchantmentUtils.enchantItem(leftItemStack.getItemHolder(), leftEnchantsMutable, rightEnchants);
                    refundXp = true;
                    returnItem = new ItemStack(compWhitePearl);
                } else if (rightItem == Items.ENCHANTED_BOOK) {
                    // Common enchant, with XP cost
                    changed = true;
                    finalResult = EnchantmentUtils.enchantItem(leftItemStack.getItemHolder(), leftEnchantsMutable, rightEnchants);
                    returnItem = new ItemStack(Items.BOOK);
                }
            }
        } else if (leftItemStack.isEnchantable()) {
            if (rightItem == compWhitePearl) {
                // Common enchant, with XP cost
                changed = true;
                finalResult = EnchantmentUtils.enchantItem(leftItemStack.getItemHolder(), leftEnchantsMutable, rightEnchants);
                returnItem = null;
            }
        }

        if (!changed) {
            event.setCanceled(true);
        } else {
            ItemStack result = leftItemStack.copy();
            EnchantmentHelper.setEnchantments(result, finalResult.targetEnchs().toImmutable());
            result.set(DataComponents.REPAIR_COST, 0);
            event.setOutput(result);
            event.setMaterialCost(1);
            event.setXpCost(EnchantmentUtils.ANVIL_BASE_COST + finalResult.xpCost());
        }
    }

    public static void onAnvilTake(AnvilCraftEvent.Post event) {
        Player player = event.getEntity();
        if (returnItem != null && !returnItem.isEmpty()) {
            player.addItem(returnItem);
        }
        if (refundXp) {
            player.giveExperienceLevels(event.getMenu().getCost());
        }
    }
}
