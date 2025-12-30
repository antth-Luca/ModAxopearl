package io.github.antthluca.axopearl.items.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

// UNIFIED CLASS, of:
// 1. items/generic/ItemBasePotion
// 2. items/generic/ItemBase
// 3. api/items/ICreativeTabMember
// 4. handlers/SuperpositionHandler
// ALL CLASSES BY Aizistral FROM https://github.com/Aizistral-Studios/Enigmatic-Legacy/tree/1.19.X (Custom License)
// https://github.com/Aizistral-Studios/Enigmatic-Legacy/blob/1.19.X/LICENSE.md
public class PotionItemBase extends Item {
    protected static final Random random = new Random();
    protected boolean isPlaceholder;
    protected List<MobEffectInstance> effectsToApply = new ArrayList<MobEffectInstance>();

    public PotionItemBase(Properties prop, MobEffectInstance... effects) {
        super(prop);
        this.isPlaceholder = false;
        for (MobEffectInstance effect : effects) {
            this.effectsToApply.add(effect);
        }
    }

    /* Item methods */
    @Override
    public void onCraftedBy(ItemStack stack, Player player) {
        // Existential void
    }

    public List<ItemStack> getCreativeTabStacks() {
        return ImmutableList.of(new ItemStack(this));
    }

    public static Properties getDefaultProperties(ResourceLocation id) {
        return new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))
            .stacksTo(1)
            .rarity(Rarity.COMMON);
    }

    public static BlockHitResult rayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
    }

    public Item setPlaceholder() {
        this.isPlaceholder = true;
        return this;
    }

    public boolean isPlaceholder() {
        return this.isPlaceholder;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component superName = super.getName(stack);
        if (this.isPlaceholder) {
            if (superName instanceof MutableComponent) {
                return ((MutableComponent) superName).withStyle(ChatFormatting.OBFUSCATED);
            }
        }
        return superName;
    }

    public static String minimizeNumber(double num) {
		int intg = (int)num;

		if (num - intg == 0)
			return "" + intg;
		else
			return "" + num;
	}

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay tooltipDisplay, Consumer<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(pStack, pContext, tooltipDisplay, components, tooltipFlag);

        // Axolotls axolotls = pStack.get(InitDataComponentTypes.AXOLOTLS.get());
        // axolotls.addToTooltip(pContext, components, tooltipFlag, pStack);
        // BlockItemStateProperties state = pStack.get(DataComponents.BLOCK_STATE);
        // components.accept(Component.translatable("container.axolotl_shelter.goo_level", state.get(AxolotlShelterBlock.GOO_LEVEL), AxolotlShelterBlock.MAX_GOO_LEVELS).withStyle(ChatFormatting.GRAY));
    }

    /* Potion methods */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity living) {
        if (living instanceof Player player) {
            this.onConsumed(worldIn, player, stack);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);

                if (stack.isEmpty()) {
                    return new ItemStack(Items.GLASS_BOTTLE);
                }

                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (this.canDrink(worldIn, playerIn, playerIn.getItemInHand(handIn))) {
            playerIn.startUsingItem(handIn);
            return super.use(worldIn, playerIn, handIn);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    public boolean canDrink(Level world, Player player, ItemStack potion) {
        return true;
    }

    public void onConsumed(Level worldIn, Player player, ItemStack potion) {
        if (player instanceof ServerPlayer) {
            for (MobEffectInstance effect : effectsToApply) {
                player.addEffect(effect);
            }
        }
    }
}
