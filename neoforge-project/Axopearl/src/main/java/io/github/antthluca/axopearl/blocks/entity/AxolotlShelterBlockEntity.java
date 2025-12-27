package io.github.antthluca.axopearl.blocks.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.antthluca.axopearl.blocks.AxolotlShelterBlock;
import io.github.antthluca.axopearl.data_components.Axolotls;
import io.github.antthluca.axopearl.init.InitBlockEntities;
import io.github.antthluca.axopearl.init.InitDataComponentTypes;
import io.github.antthluca.axopearl.utils.AxopearlTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.debug.DebugValueSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AxolotlShelterBlockEntity extends BlockEntity {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final String AXOLOTLS = "axolotls";
    static final List<String> IGNORED_AXOLOTL_TAGS = Arrays.asList(
            "Air",
            "drop_chances",
            "equipment",
            "Brain",
            "CanPickUpLoot",
            "DeathTime",
            "fall_distance",
            "FallFlying",
            "Fire",
            "HurtByTimestamp",
            "HurtTime",
            "LeftHanded",
            "Motion",
            "NoGravity",
            "OnGround",
            "PortalCooldown",
            "Pos",
            "Rotation",
            "sleeping_pos",
            "Passengers",
            "leash",
            "UUID");
    public static final int MAX_OCCUPANTS = 3;
    private static final int TICKS_BEFORE_REENTERING = 2400;
    private static final int OCCUPATION_TICKS = 1600;
    private final List<AxolotlShelterBlockEntity.AxolotlData> stored = Lists.newArrayList();

    // SUPER
    @Override
    public void setChanged() {
        if (this.isFireNearby()) {
            this.emptyAllLivingFromShelter(null, this.level.getBlockState(this.getBlockPos()));
        }

        super.setChanged();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.stored.clear();
        input.read(AXOLOTLS, AxolotlShelterBlockEntity.Occupant.LIST_CODEC).orElse(List.of())
                .forEach(this::storeAxolotl);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(AXOLOTLS, AxolotlShelterBlockEntity.Occupant.LIST_CODEC, this.getAxolotls());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        this.stored.clear();
        List<Occupant> list = ((Axolotls) dataComponentGetter.getOrDefault(InitDataComponentTypes.AXOLOTLS.get(), Axolotls.EMPTY)).axolotls();
        list.forEach(this::storeAxolotl);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder dataComponentMap$builder) {
        super.collectImplicitComponents(dataComponentMap$builder);
        dataComponentMap$builder.set(InitDataComponentTypes.AXOLOTLS, new Axolotls(this.getAxolotls()));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(AXOLOTLS);
    }

    @Override
    public void registerDebugValues(ServerLevel level, DebugValueSource.Registration debugValueSource$registration) {
        return;
    }

    // MAIN
    public AxolotlShelterBlockEntity(BlockPos pos, BlockState state) {
        super(InitBlockEntities.AXOLOTL_SHELTER_BE.get(), pos, state);
    }

    public boolean isFireNearby() {
        if (this.level == null) {
            return false;
        } else {
            for (BlockPos blockpos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1),
                    this.worldPosition.offset(1, 1, 1))) {
                if (this.level.getBlockState(blockpos).getBlock() instanceof FireBlock) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public boolean isFull() {
        return this.stored.size() == MAX_OCCUPANTS;
    }

    public void emptyAllLivingFromShelter(@Nullable Player player, BlockState state) {
        List<Entity> list = this.releaseAllOccupants(state);
        if (player != null) {
            for (Entity entity : list) {
                if (entity instanceof Axolotl axolotl && player.position().distanceToSqr(entity.position()) <= 16.0) {
                    axolotl.setTarget(player);
                }
            }
        }
    }

    private List<Entity> releaseAllOccupants(BlockState state) {
        List<Entity> list = Lists.newArrayList();
        this.stored
                .removeIf(axolotlData -> releaseOccupant(this.level, this.worldPosition, state,
                        axolotlData.toOccupant(), list));
        if (!list.isEmpty()) {
            super.setChanged();
        }

        return list;
    }

    public int getOccupantCount() {
        return this.stored.size();
    }

    public static int getGooLevel(BlockState state) {
        return state.getValue(AxolotlShelterBlock.GOO_LEVEL);
    }

    public void addOccupant(Axolotl axolotl) {
        if (this.stored.size() < MAX_OCCUPANTS) {
            axolotl.stopRiding();
            axolotl.ejectPassengers();
            axolotl.dropLeash();
            this.storeAxolotl(AxolotlShelterBlockEntity.Occupant.of(axolotl));
            if (this.level != null) {
                BlockPos blockpos = this.getBlockPos();
                this.level
                        .playSound(
                                null,
                                (double) blockpos.getX(),
                                (double) blockpos.getY(),
                                (double) blockpos.getZ(),
                                SoundEvents.BEEHIVE_ENTER,
                                SoundSource.BLOCKS,
                                1.0F,
                                1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos,
                        GameEvent.Context.of(axolotl, this.getBlockState()));
            }

            axolotl.discard();
            super.setChanged();
        }
    }

    public void storeAxolotl(AxolotlShelterBlockEntity.Occupant occupant) {
        this.stored.add(new AxolotlShelterBlockEntity.AxolotlData(occupant));
    }

    private static boolean releaseOccupant(Level level, BlockPos pos, BlockState state,
            AxolotlShelterBlockEntity.Occupant occupant, @Nullable List<Entity> storedInShelters) {
        if (Bee.isNightOrRaining(level)) {
            return false;
        } else {
            Direction direction = state.getValue(AxolotlShelterBlock.FACING);
            BlockPos blockpos = pos.relative(direction);
            boolean flag = !level.getBlockState(blockpos).getCollisionShape(level, blockpos).isEmpty();
            if (flag) {
                return false;
            } else {
                Entity entity = occupant.createEntity(level, pos);
                if (entity != null) {
                    if (entity instanceof Axolotl axolotl) {
                        if (storedInShelters != null) {
                            storedInShelters.add(axolotl);
                        }

                        float f = entity.getBbWidth();
                        double d3 = flag ? 0.0 : 0.55 + f / 2.0F;
                        double d0 = pos.getX() + 0.5 + d3 * direction.getStepX();
                        double d1 = pos.getY() + 0.5 - entity.getBbHeight() / 2.0F;
                        double d2 = pos.getZ() + 0.5 + d3 * direction.getStepZ();
                        entity.snapTo(d0, d1, d2, entity.getYRot(), entity.getXRot());
                    }

                    level.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos,
                            GameEvent.Context.of(entity, level.getBlockState(pos)));
                    return level.addFreshEntity(entity);
                } else {
                    return false;
                }
            }
        }
    }

    private static void tickOccupants(
            Level level, BlockPos pos, BlockState state, List<AxolotlShelterBlockEntity.AxolotlData> data) {
        boolean flag = false;
        Iterator<AxolotlShelterBlockEntity.AxolotlData> iterator = data.iterator();

        while (iterator.hasNext()) {
            AxolotlShelterBlockEntity.AxolotlData axolotlData = iterator.next();
            if (axolotlData.tick()) {
                if (releaseOccupant(
                        level, pos, state, axolotlData.toOccupant(), null)) {
                    flag = true;
                    iterator.remove();
                }
            }
        }

        if (flag) {
            setChanged(level, pos, state);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AxolotlShelterBlockEntity axolotlShelter) {
        tickOccupants(level, pos, state, axolotlShelter.stored);
        if (!axolotlShelter.stored.isEmpty() && level.getRandom().nextDouble() < 0.005) {
            double d0 = pos.getX() + 0.5;
            double d1 = pos.getY();
            double d2 = pos.getZ() + 0.5;
            level.playSound(null, d0, d1, d2, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private List<AxolotlShelterBlockEntity.Occupant> getAxolotls() {
        return this.stored.stream().map(AxolotlShelterBlockEntity.AxolotlData::toOccupant).toList();
    }

    // INTERNAL
    static class AxolotlData {
        private final AxolotlShelterBlockEntity.Occupant occupant;
        private int ticksInShelter;

        // MAIN
        AxolotlData(AxolotlShelterBlockEntity.Occupant occupant) {
            this.occupant = occupant;
            this.ticksInShelter = occupant.ticksInShelter();
        }

        public boolean tick() {
            return this.ticksInShelter++ > this.occupant.minTicksInShelter;
        }

        public AxolotlShelterBlockEntity.Occupant toOccupant() {
            return new AxolotlShelterBlockEntity.Occupant(this.occupant.entityData, this.ticksInShelter,
                    this.occupant.minTicksInShelter);
        }
    }

    public record Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInShelter, int minTicksInShelter) {
        public static final Codec<AxolotlShelterBlockEntity.Occupant> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data")
                                .forGetter(AxolotlShelterBlockEntity.Occupant::entityData),
                        Codec.INT.fieldOf("ticks_in_shelter")
                                .forGetter(AxolotlShelterBlockEntity.Occupant::ticksInShelter),
                        Codec.INT.fieldOf("min_ticks_in_shelter")
                                .forGetter(AxolotlShelterBlockEntity.Occupant::minTicksInShelter))
                        .apply(instance, AxolotlShelterBlockEntity.Occupant::new));
        public static final Codec<List<AxolotlShelterBlockEntity.Occupant>> LIST_CODEC = CODEC.listOf();
        public static final StreamCodec<RegistryFriendlyByteBuf, AxolotlShelterBlockEntity.Occupant> STREAM_CODEC = StreamCodec
                .composite(
                        TypedEntityData.streamCodec(EntityType.STREAM_CODEC),
                        AxolotlShelterBlockEntity.Occupant::entityData,
                        ByteBufCodecs.VAR_INT,
                        AxolotlShelterBlockEntity.Occupant::ticksInShelter,
                        ByteBufCodecs.VAR_INT,
                        AxolotlShelterBlockEntity.Occupant::minTicksInShelter,
                        AxolotlShelterBlockEntity.Occupant::new);

        public static AxolotlShelterBlockEntity.Occupant of(Entity entity) {
            AxolotlShelterBlockEntity.Occupant AxolotlShelterBlockEntity$occupant;
            try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(
                    entity.problemPath(), AxolotlShelterBlockEntity.LOGGER)) {
                TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(problemreporter$scopedcollector,
                        entity.registryAccess());
                entity.save(tagvalueoutput);
                AxolotlShelterBlockEntity.IGNORED_AXOLOTL_TAGS.forEach(tagvalueoutput::discard);
                CompoundTag compoundtag = tagvalueoutput.buildResult();
                AxolotlShelterBlockEntity$occupant = new AxolotlShelterBlockEntity.Occupant(
                        TypedEntityData.of(entity.getType(), compoundtag), 0, OCCUPATION_TICKS);
            }

            return AxolotlShelterBlockEntity$occupant;
        }

        public static AxolotlShelterBlockEntity.Occupant create(int ticksInShelter) {
            return new AxolotlShelterBlockEntity.Occupant(TypedEntityData.of(EntityType.AXOLOTL, new CompoundTag()),
                    ticksInShelter, OCCUPATION_TICKS);
        }

        @Nullable
        public Entity createEntity(Level level, BlockPos pos) {
            CompoundTag compoundtag = this.entityData.copyTagWithoutId();
            AxolotlShelterBlockEntity.IGNORED_AXOLOTL_TAGS.forEach(compoundtag::remove);
            Entity entity = EntityType.loadEntityRecursive(this.entityData.type(), compoundtag, level,
                    EntitySpawnReason.LOAD, ent -> ent);
            if (entity != null && entity.getType().is(AxopearlTags.EntityTypeTags.AXOLOTL_SHELTER_INHABITORS)) {
                entity.setNoGravity(true);
                if (entity instanceof Axolotl axolotl) {
                    setAxolotlReleaseData(this.ticksInShelter, axolotl);
                }

                return entity;
            } else {
                return null;
            }
        }

        private static void setAxolotlReleaseData(int ticksInShelter, Axolotl axolotl) {
            int i = axolotl.getAge();
            if (i < 0) {
                axolotl.setAge(Math.min(0, i + ticksInShelter));
            } else if (i > 0) {
                axolotl.setAge(Math.max(0, i - ticksInShelter));
            }

            axolotl.setInLoveTime(Math.max(0, axolotl.getInLoveTime() - ticksInShelter));
        }
    }
}
