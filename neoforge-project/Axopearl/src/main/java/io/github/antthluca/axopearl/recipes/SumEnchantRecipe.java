package io.github.antthluca.axopearl.recipes;

import java.util.Iterator;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.antthluca.axopearl.init.InitItems;
import io.github.antthluca.axopearl.init.InitRecipeSerializers;
import io.github.antthluca.axopearl.utils.EnchantmentUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class SumEnchantRecipe extends CustomRecipe {
    private final Ingredient toEnchItem;
    private final Ingredient enchsSourceItem;
    private final ItemStack returnItem;

    // Constructor
    public SumEnchantRecipe(Ingredient toEnch, Ingredient enchsSource, ItemStack toReturn) {
        super(CraftingBookCategory.MISC);
        this.toEnchItem = toEnch;
        this.enchsSourceItem = enchsSource;
        this.returnItem = toReturn;
    }

    // Getters and Setters
    public Ingredient getToEnch() {
        return this.toEnchItem;
    }

    public Ingredient getEnchsSource() {
        return this.enchsSourceItem;
    }

    public ItemStack getReturn() {
        return this.returnItem;
    }

    // Override
    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean foundToEnch = false;
        boolean foundEnchsSource = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();

            if (this.toEnchItem.test(stack)) {
                if (foundToEnch) return false;
                foundToEnch = true;
            } else if (this.enchsSourceItem.test(stack)) {
                if (foundEnchsSource) return false;
                foundEnchsSource = true;
            } else {
                return false;
            }
        }

        return foundToEnch && foundEnchsSource;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
        ItemStack toEnchStack = ItemStack.EMPTY;
        ItemStack enchsSourceStack = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (this.toEnchItem.test(stack)) {
                toEnchStack = stack;
            } else if (this.enchsSourceItem.test(stack)) {
                enchsSourceStack = stack;
            }
        }

        if (toEnchStack.isEmpty() || enchsSourceStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        ItemEnchantments bookEnchs = enchsSourceStack.get(EnchantmentHelper.getComponentType(enchsSourceStack));
        ItemEnchantments.Mutable bookEnchsMutable = new ItemEnchantments.Mutable(bookEnchs);

        ItemEnchantments pearlEnchs = EnchantmentHelper.getEnchantmentsForCrafting(toEnchStack);
        ItemEnchantments.Mutable pearlEnchsMutable = new ItemEnchantments.Mutable(pearlEnchs);

        Iterator iterator = bookEnchs.entrySet().iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            Object2IntMap.Entry<Holder<Enchantment>> bookEntry = (Object2IntMap.Entry) iterator.next();
            Holder<Enchantment> bookHolder = bookEntry.getKey();
            if (EnchantmentUtils.canEnchantPerCompatibility(pearlEnchsMutable, bookHolder)) {
                int bookLevel = bookEntry.getIntValue();
                int pearlLevel = pearlEnchsMutable.getLevel(bookHolder);
                int finalLevel = pearlLevel == bookLevel ? bookLevel + 1 : Math.max(pearlLevel, bookLevel);
                if (finalLevel != pearlLevel) {
                    changed = true;
                    pearlEnchsMutable.set(bookHolder, finalLevel);
                }
            }
        }
        if (changed) {
            ItemStack result = toEnchStack.copy();
            if (result.getItem() == InitItems.WHITE_TECHPEARL.value()) {
                result = new ItemStack(InitItems.ENCHANTED_TECHPEARL.value());
            }
            EnchantmentHelper.setEnchantments(result, pearlEnchsMutable.toImmutable());
            return result;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(input);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (this.enchsSourceItem.test(stack)) {
                remaining.set(i, this.returnItem);
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<SumEnchantRecipe> getSerializer() {
        return InitRecipeSerializers.SUM_ENCHANT_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<SumEnchantRecipe> {
        public static final MapCodec<SumEnchantRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC.fieldOf("ingredient_to_ench").forGetter(recipe -> recipe.toEnchItem),
            Ingredient.CODEC.fieldOf("ingredient_ench_source").forGetter(recipe -> recipe.enchsSourceItem),
            ItemStack.STRICT_CODEC.fieldOf("return_item").forGetter(recipe -> recipe.returnItem)
        ).apply(builder, SumEnchantRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SumEnchantRecipe> STREAM_CODEC = StreamCodec.of(
            SumEnchantRecipe.Serializer::toNetwork, SumEnchantRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SumEnchantRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SumEnchantRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SumEnchantRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient toEnchsItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient enchsSourceItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack returnStack = ItemStack.STREAM_CODEC.decode(buffer);

            return new SumEnchantRecipe(toEnchsItem, enchsSourceItem, returnStack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SumEnchantRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.toEnchItem);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.enchsSourceItem);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.returnItem);
        }
    }
}
