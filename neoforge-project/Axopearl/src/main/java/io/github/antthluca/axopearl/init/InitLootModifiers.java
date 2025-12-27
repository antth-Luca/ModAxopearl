package io.github.antthluca.axopearl.init;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.loot.ReplaceItemModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class InitLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(
        NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Axopearl.MODID);

    // Loot Modifiers
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> REPLACE_ITEM = LOOT_MODIFIERS.register(
        "replace_item", () -> ReplaceItemModifier.CODEC);
}
