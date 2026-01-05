package io.github.antthluca.axopearl.events;

import java.util.Iterator;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.init.InitItems;
import io.github.antthluca.axopearl.utils.EnchantmentUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = Axopearl.MODID)
public class WorldEvents {
    private static ItemStack returnItem;
    private static boolean refundXp;

    @SubscribeEvent
    public static void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack rightItemStack = event.getRight();
        Item rightItem = rightItemStack.getItem();
        ItemStack leftItemStack = event.getLeft();
        Item leftItem = leftItemStack.getItem();
        // (->) Is White Pearl
        if (rightItem == InitItems.ENCHANTED_TECHPEARL.value()) {
            ItemEnchantments rightEnchants = rightItemStack.get(DataComponents.ENCHANTMENTS);
            // In case unenchanted White Pearl
            if (!rightItemStack.isEnchanted()) {
                event.setCanceled(true);
                return;
            }

            boolean changed = false;
            ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(leftItemStack);
            ItemEnchantments.Mutable leftEnchantMutable = new ItemEnchantments.Mutable(leftEnchants);
            int expCost = EnchantmentUtils.ANVIL_BASE_COST;
            // (<-) Is any enchantable item
            if (leftItemStack.isEnchantable()) {
                // Enchanting the left item
                Iterator iterator = rightEnchants.entrySet().iterator();
                while (iterator.hasNext()) {
                    Object2IntMap.Entry<Holder<Enchantment>> rightEntry = (Object2IntMap.Entry) iterator.next();
                    Holder<Enchantment> rightKey = (Holder) rightEntry.getKey();
                    if (EnchantmentUtils.canEnchantPerSupport(leftItemStack.getItemHolder(), rightKey)) {
                        if (EnchantmentUtils.canEnchantPerCompatibility(leftEnchantMutable, rightKey)) {
                            int rightLevel = rightEntry.getIntValue();
                            int leftLevel = leftEnchantMutable.getLevel(rightKey);
                            int finalLevel = leftLevel == rightLevel ? rightLevel + 1 : Math.max(leftLevel, rightLevel);
                            if (finalLevel != leftLevel) {
                                changed = true;
                                leftEnchantMutable.set(rightKey, finalLevel);
                                expCost += finalLevel - 1;
                            }
                        }
                    }
                }
            }
            // Event
            if (!changed) {
                event.setCanceled(true);
            } else {
                ItemStack result = leftItemStack.copy();
                EnchantmentHelper.setEnchantments(result, leftEnchantMutable.toImmutable());
                event.setOutput(result);
                event.setMaterialCost(1);
                event.setXpCost(expCost);
            }
        }
    }
}
