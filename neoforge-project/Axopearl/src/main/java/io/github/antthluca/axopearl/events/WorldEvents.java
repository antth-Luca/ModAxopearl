package io.github.antthluca.axopearl.events;

import java.util.Iterator;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.init.InitItems;
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
    @SubscribeEvent
    public static void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack rightItemStack = event.getRight();
        Item rightItem = rightItemStack.getItem();
        ItemStack leftItemStack = event.getLeft();
        Item leftItem = leftItemStack.getItem();

        // (->) Is White Pearl
        if (rightItem == InitItems.WHITE_TECHPEARL.value()) {
            ItemEnchantments rightEnchants = rightItemStack.get(DataComponents.ENCHANTMENTS);
            // In case unenchanted White Pearl
            if (rightEnchants == null || rightEnchants.isEmpty()) {
                event.setCanceled(true);
                return;
            }

            boolean changed = false;
            ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(leftItemStack);
            ItemEnchantments.Mutable leftEnchantMutable = new ItemEnchantments.Mutable(leftEnchants);
            int expCost = 1;
            // (<-) Is any enchantable item
            if (leftItemStack.isEnchantable()) {
                // Enchanting the left item
                Iterator iterator = rightEnchants.entrySet().iterator();
                while (iterator.hasNext()) {
                    Object2IntMap.Entry<Holder<Enchantment>> rightEntry = (Object2IntMap.Entry) iterator.next();
                    Holder<Enchantment> rightKey = (Holder) rightEntry.getKey();
                    boolean canEnchant = ((Enchantment) rightKey.value()).definition().supportedItems().contains(leftItemStack.getItemHolder());
                    if (canEnchant) {
                        for (Holder<Enchantment> leftKey : leftEnchantMutable.keySet()) {
                            if (!Enchantment.areCompatible(rightKey, leftKey)) {
                                canEnchant = false;
                                break;
                            }
                        }

                        if (canEnchant) {
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
            // (<-) Is White Pearl
            } else if (leftItemStack.is(InitItems.WHITE_TECHPEARL)) {
                // TODO: Adicionar a soma de duas pérolas brancas;
            }
            // Event
            if (!changed) {
                event.setCanceled(true);
            } else {
                ItemStack result = leftItemStack.copy();
                EnchantmentHelper.setEnchantments(result, leftEnchantMutable.toImmutable());
                result.set(DataComponents.REPAIR_COST, 0);
                event.setOutput(result);
                event.setMaterialCost(1);
                event.setXpCost(Math.min(expCost, 39));
            }
        }
    }
}
