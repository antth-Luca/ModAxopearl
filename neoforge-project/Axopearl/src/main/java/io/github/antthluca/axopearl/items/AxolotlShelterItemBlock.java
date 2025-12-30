package io.github.antthluca.axopearl.items;

import java.util.function.Consumer;

import io.github.antthluca.axopearl.blocks.AxolotlShelterBlock;
import io.github.antthluca.axopearl.data_components.Axolotls;
import io.github.antthluca.axopearl.init.InitBlocks;
import io.github.antthluca.axopearl.init.InitDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TooltipDisplay;

public class AxolotlShelterItemBlock extends BlockItem {
    public AxolotlShelterItemBlock(ResourceLocation id) {
        super(
            InitBlocks.AXOLOTL_SHELTER.get(),
            new Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .component(InitDataComponentTypes.AXOLOTLS.get(), Axolotls.EMPTY)
                .component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(AxolotlShelterBlock.GOO_LEVEL, 0)));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay tooltipDisplay, Consumer<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(pStack, pContext, tooltipDisplay, components, tooltipFlag);

        Axolotls axolotls = pStack.get(InitDataComponentTypes.AXOLOTLS.get());
        axolotls.addToTooltip(pContext, components, tooltipFlag, pStack);
        BlockItemStateProperties state = pStack.get(DataComponents.BLOCK_STATE);
        components.accept(Component.translatable("container.axolotl_shelter.goo_level", state.get(AxolotlShelterBlock.GOO_LEVEL), AxolotlShelterBlock.MAX_GOO_LEVELS).withStyle(ChatFormatting.GRAY));
    }
}
