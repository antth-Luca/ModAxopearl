package io.github.antthluca.axopearl.data_components;

import java.util.List;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;

import io.github.antthluca.axopearl.blocks.entity.AxolotlShelterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record Axolotls(List<AxolotlShelterBlockEntity.Occupant> axolotls) implements TooltipProvider {
    public static final Codec<Axolotls> CODEC = AxolotlShelterBlockEntity.Occupant.LIST_CODEC.xmap(Axolotls::new, Axolotls::axolotls);
    public static final StreamCodec<RegistryFriendlyByteBuf, Axolotls> STREAM_CODEC = AxolotlShelterBlockEntity.Occupant.STREAM_CODEC
        .apply(ByteBufCodecs.list())
        .map(Axolotls::new, Axolotls::axolotls);
    public static final Axolotls EMPTY = new Axolotls(List.of());

    @Override
    public void addToTooltip(Item.TooltipContext item$tooltipCtx, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        consumer.accept(Component.translatable("container.axolotl_shelter.axolotls", this.axolotls.size(), AxolotlShelterBlockEntity.MAX_OCCUPANTS).withStyle(ChatFormatting.GRAY));
    }
}
