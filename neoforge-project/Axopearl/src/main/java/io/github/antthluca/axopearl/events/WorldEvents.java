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

        boolean leftEnchantable = leftItemStack.isEnchantable();
        boolean rightIsPearl = (rightItemStack.isEnchantable() && rightItem == InitItems.WHITE_TECHPEARL.value());
        System.out.println(String.format("%s | %s", leftEnchantable, rightIsPearl));
        System.out.println(String.format("rInput: %s | init: %s", rightItem, InitItems.WHITE_TECHPEARL.value()));
        if (leftEnchantable && rightIsPearl) {
            ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(leftItemStack);
            ItemEnchantments rightEnchants = rightItemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments.Mutable enchantMutable = new ItemEnchantments.Mutable(leftEnchants);
            boolean changed = false;
            Iterator iterator = rightEnchants.entrySet().iterator();

            while (iterator.hasNext()) {
                Object2IntMap.Entry<Holder<Enchantment>> entry = (Object2IntMap.Entry) iterator.next();
                Holder<Enchantment> key = (Holder) entry.getKey();
                boolean canEnchant = ((Enchantment) key.value()).definition().supportedItems().contains(leftItemStack.getItemHolder());
                if (canEnchant) {
                    int rightLevel = entry.getIntValue();
                    int leftLevel = enchantMutable.getLevel(key);
                    int finalLevel = Math.max(leftLevel, rightLevel);
                    enchantMutable.set(key, finalLevel);
                    if (finalLevel != leftLevel) {
                        changed = true;
                    }
                }
            }

            if (!changed) {
                event.setCanceled(true);
            } else {
                ItemStack result = leftItemStack.copy();
                EnchantmentHelper.setEnchantments(result, enchantMutable.toImmutable());
                event.setOutput(result);
                event.setMaterialCost(1);
            }
        }
    }
}
