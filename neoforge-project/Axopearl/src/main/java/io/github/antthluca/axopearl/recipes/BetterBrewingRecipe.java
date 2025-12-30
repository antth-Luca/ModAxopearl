package io.github.antthluca.axopearl.recipes;

import io.github.antthluca.axopearl.utils.PotionUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.registries.DeferredItem;

// BetterBrewingRecipe Class by CAS-ual-TY from https://github.com/CAS-ual-TY/Extra-Potions (GPL-3.0 License)
// https://github.com/CAS-ual-TY/Extra-Potions/blob/main/LICENSE
public class BetterBrewingRecipe implements IBrewingRecipe {
    private final Holder<Potion> input;
    private final Item ingredient;
    private final DeferredItem<Item> output;

    public BetterBrewingRecipe(Holder<Potion> input, Item ingredient, DeferredItem<Item> output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack inpStack) {
        return inpStack != null && PotionUtils.getPotion(inpStack) == this.input.value();
    }

    @Override
    public boolean isIngredient(ItemStack ingStack) {
        return ingStack != null && ingStack.getItem() == this.ingredient;
    }

    @Override
    public ItemStack getOutput(ItemStack inpStack, ItemStack ingStack) {
        return this.isInput(inpStack) && this.isIngredient(ingStack) ? new ItemStack(this.getOutput()) : ItemStack.EMPTY;
    }

    public Item getOutput() {
        return this.output.asItem();
    }
}
