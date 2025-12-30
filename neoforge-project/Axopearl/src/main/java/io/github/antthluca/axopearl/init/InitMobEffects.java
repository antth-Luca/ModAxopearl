package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.mob_effects.AxolotlBurstOfEnergyMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(
    BuiltInRegistries.MOB_EFFECT, Axopearl.MODID);

    // Mob Effects
    public static final Holder<MobEffect> AXOLOTL_BURST_OF_ENERGY = MOB_EFFECTS.register(
        "axolotl_burst_of_energy", AxolotlBurstOfEnergyMobEffect::new);
}
