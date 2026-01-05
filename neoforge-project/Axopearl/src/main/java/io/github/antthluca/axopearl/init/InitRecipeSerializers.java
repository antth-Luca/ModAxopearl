package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.recipes.SumEnchantRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
        Registries.RECIPE_SERIALIZER, Axopearl.MODID
    );

    // Recipe Serializers
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SumEnchantRecipe>> SUM_ENCHANT_RECIPE = RECIPE_SERIALIZERS.register(
        "sum_enchant_recipe", SumEnchantRecipe.Serializer::new);
}
