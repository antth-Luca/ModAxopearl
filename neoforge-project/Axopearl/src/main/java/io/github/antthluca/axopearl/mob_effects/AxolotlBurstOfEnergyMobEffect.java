package io.github.antthluca.axopearl.mob_effects;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class AxolotlBurstOfEnergyMobEffect extends MobEffect {
    public AxolotlBurstOfEnergyMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xf3add6);

        addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, "axolotl_burst_of_energy"),
            0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(ServerLevel sLevel, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            player.causeFoodExhaustion(0.005F * (float) (amplifier + 1));
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
