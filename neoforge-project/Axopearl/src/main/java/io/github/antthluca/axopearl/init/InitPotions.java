package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.items.custom.PotionItemBase;
import io.github.antthluca.axopearl.recipes.BetterBrewingRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitPotions {
    public static final DeferredRegister.Items POTION_ITEMS = DeferredRegister.createItems(Axopearl.MODID);

    // Potions
    public static final DeferredItem<Item> POTION_OF_AXOLOTL_BURST_OF_ENERGY = POTION_ITEMS.register(
        "potion_of_axolotl_burst_of_energy", idResLoc -> new PotionItemBase(
            PotionItemBase.getDefaultProperties(idResLoc),
            new MobEffectInstance(InitMobEffects.AXOLOTL_BURST_OF_ENERGY, 3600)));

    // Recipes
    @EventBusSubscriber(modid = Axopearl.MODID)
    class Recipes {
        @SubscribeEvent
        public static void registerPotionsWithRecipes(RegisterBrewingRecipesEvent event) {
            PotionBrewing.Builder builder = event.getBuilder();

            // Recipes and Potions
            builder.addRecipe(new BetterBrewingRecipe(
                Potions.SWIFTNESS,
                Items.AXOLOTL_BUCKET,
                POTION_OF_AXOLOTL_BURST_OF_ENERGY));
        }
    }
}
