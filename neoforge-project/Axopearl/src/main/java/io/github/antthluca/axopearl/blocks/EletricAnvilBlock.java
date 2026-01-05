package io.github.antthluca.axopearl.blocks;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import io.github.antthluca.axopearl.screens.EletricAnvilMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class EletricAnvilBlock extends BaseEntityBlock {
    public static final MapCodec<EletricAnvilBlock> CODEC = simpleCodec(EletricAnvilBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    // SUPER
    @Override
    public MapCodec<EletricAnvilBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_ANVIL);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    @Nullable
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, playerInv, player) -> new EletricAnvilMenu(containerId, playerInv), Component.translatable("block.axopearl.eletric_anvil"));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.setValue(FACING, rotation.rotate((Direction) state.getValue(FACING)));
    }

    // MAIN
    public EletricAnvilBlock(BlockBehaviour.Properties prop) {
        super(prop);
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
    }
}
