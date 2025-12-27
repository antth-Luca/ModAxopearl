package io.github.antthluca.axopearl.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import io.github.antthluca.axopearl.blocks.entity.AxolotlShelterBlockEntity;
import io.github.antthluca.axopearl.init.InitBlockEntities;
import io.github.antthluca.axopearl.init.InitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;

public class AxolotlShelterBlock extends BaseEntityBlock {
    public static final MapCodec<AxolotlShelterBlock> CODEC = simpleCodec(AxolotlShelterBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final int MAX_GOO_LEVELS = 5;
    public static final IntegerProperty GOO_LEVEL = IntegerProperty.create("goo_level", 0, MAX_GOO_LEVELS);

    // SUPER
    @Override
    public MapCodec<AxolotlShelterBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction dir) {
        return (Integer)state.getValue(GOO_LEVEL);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEnt, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEnt, stack);
        if (!level.isClientSide() && blockEnt instanceof AxolotlShelterBlockEntity axolotlShelterBlockEntity) {
            if (!EnchantmentHelper.hasTag(stack, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                axolotlShelterBlockEntity.emptyAllLivingFromShelter(player, state);
                Containers.updateNeighboursAfterDestroy(state, level, pos);
            }

            // TODO: Add advancement;
            //CriteriaTriggers.AXOLOTL_SHELTER_DESTROYED.trigger((ServerPlayer)player, state, stack, axolotlShelterBlockEntity.getOccupantCount());
        }

    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int currentLevel = state.getValue(GOO_LEVEL);
        boolean flag = false;
        if (currentLevel >= MAX_GOO_LEVELS) {
            Item item = stack.getItem();
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel) level;
                if (stack.canPerformAction(ItemAbilities.SHEARS_HARVEST)) {
                    dropAxolotlGoo(serverLevel, pos);
                    level.playSound((Entity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                    stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                    flag = true;
                    level.gameEvent(player, GameEvent.SHEAR, pos);
                }
            }

            if (!level.isClientSide() && flag) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
        }

        if (flag) {
            this.resetGooLevel(level, state, pos);

            return InteractionResult.SUCCESS;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rSource) {
        if ((Integer)state.getValue(GOO_LEVEL) >= MAX_GOO_LEVELS) {
            for(int i = 0; i < rSource.nextInt(1) + 1; ++i) {
                this.trySpawnDripParticles(level, pos, state);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{GOO_LEVEL, FACING});
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel serverlevel) {
            if (player.preventsBlockDrops() && serverlevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                BlockEntity blockEnt = level.getBlockEntity(pos);
                if (blockEnt instanceof AxolotlShelterBlockEntity) {
                    AxolotlShelterBlockEntity axolotlShelterBlockEntity = (AxolotlShelterBlockEntity) blockEnt;
                    int currentGoo = (Integer)state.getValue(GOO_LEVEL);
                    boolean flag = !axolotlShelterBlockEntity.isEmpty();
                    if (flag || currentGoo > 0) {
                        ItemStack itemstack = new ItemStack(this);
                        itemstack.applyComponents(axolotlShelterBlockEntity.collectComponents());
                        itemstack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(GOO_LEVEL, currentGoo));
                        ItemEntity itemEntity = new ItemEntity(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemstack);
                        itemEntity.setDefaultPickUpDelay();
                        level.addFreshEntity(itemEntity);
                    }
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParams) {
        Entity entity = (Entity) lootParams.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) {
            BlockEntity blockentity = (BlockEntity) lootParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockentity instanceof AxolotlShelterBlockEntity) {
                AxolotlShelterBlockEntity axolotlShelterBlockEntity = (AxolotlShelterBlockEntity) blockentity;
                axolotlShelterBlockEntity.emptyAllLivingFromShelter((Player) null, state);
            }
        }

        return super.getDrops(state, lootParams);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader levelReader, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemstack = super.getCloneItemStack(levelReader, pos, state, includeData);
        if (includeData) {
            itemstack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(GOO_LEVEL, (Integer)state.getValue(GOO_LEVEL)));
        }

        return itemstack;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduleTickAccess, BlockPos pos, Direction dir, BlockPos neighborPos, BlockState neighborState, RandomSource rSource) {
        if (levelReader.getBlockState(neighborPos).getBlock() instanceof FireBlock) {
            BlockEntity blockEnt = levelReader.getBlockEntity(pos);
            if (blockEnt instanceof AxolotlShelterBlockEntity) {
                AxolotlShelterBlockEntity axolotlShelterBlockEntity = (AxolotlShelterBlockEntity) blockEnt;
                axolotlShelterBlockEntity.emptyAllLivingFromShelter((Player) null, state);
            }
        }

        return super.updateShape(state, levelReader, scheduleTickAccess, pos, dir, neighborPos, neighborState, rSource);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    // IMPLEMENTS
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AxolotlShelterBlockEntity(pos, state);
    };

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, InitBlockEntities.AXOLOTL_SHELTER_BE.get(), AxolotlShelterBlockEntity::serverTick);
    }

    // MAIN
    public AxolotlShelterBlock(BlockBehaviour.Properties prop) {
        super(prop);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(GOO_LEVEL, 0)).setValue(FACING, Direction.NORTH));
    }

    public static void dropAxolotlGoo(ServerLevel level, BlockPos pos) {
        ItemStack stack = InitItems.AXOLOTL_GOO.toStack();
        popResource(level, pos, stack);
    }

    public void resetGooLevel(Level level, BlockState state, BlockPos pos) {
        level.setBlock(pos, (BlockState) state.setValue(GOO_LEVEL, 0), 3);
    }

    private boolean shelterContainsAxolotls(Level level, BlockPos pos) {
        BlockEntity block_ent = level.getBlockEntity(pos);
        boolean contains;
        if (block_ent instanceof AxolotlShelterBlockEntity axolotlsheltBlockEntity) {
            contains = !axolotlsheltBlockEntity.isEmpty();
        } else {
            contains = false;
        }

        return contains;
    }

    private void trySpawnDripParticles(Level level, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(level.random.nextFloat() < 0.3F)) {
            VoxelShape voxelshape = state.getCollisionShape(level, pos);
            double y1 = voxelshape.max(Axis.Y);
            if (y1 >= (double)1.0F && !state.is(BlockTags.IMPERMEABLE)) {
                double y2 = voxelshape.min(Axis.Y);
                if (y2 > (double)0.0F) {
                    this.spawnParticle(level, pos, voxelshape, (double)pos.getY() + y2 - 0.05);
                } else {
                    BlockPos blockpos = pos.below();
                    BlockState blockstate = level.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(level, blockpos);
                    double shape1_y1 = voxelshape1.max(Axis.Y);
                    if ((shape1_y1 < (double)1.0F || !blockstate.isCollisionShapeFullBlock(level, blockpos)) && blockstate.getFluidState().isEmpty()) {
                        this.spawnParticle(level, pos, voxelshape, (double)pos.getY() - 0.05);
                    }
                }
            }
        }
    }

    private void spawnParticle(Level level, BlockPos pos, VoxelShape shape, double y) {
        this.spawnFluidParticle(level, (double)pos.getX() + shape.min(Axis.X), (double)pos.getX() + shape.max(Axis.X), (double)pos.getZ() + shape.min(Axis.Z), (double)pos.getZ() + shape.max(Axis.Z), y);
    }

    private void spawnFluidParticle(Level particleData, double x1, double x2, double z1, double z2, double y) {
        particleData.addParticle(ParticleTypes.DRIPPING_WATER, Mth.lerp(particleData.random.nextDouble(), x1, x2), y, Mth.lerp(particleData.random.nextDouble(), z1, z2), (double)0.0F, (double)0.0F, (double)0.0F);
    }
}
