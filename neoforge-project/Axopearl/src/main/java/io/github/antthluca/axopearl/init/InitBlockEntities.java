package io.github.antthluca.axopearl.init;

import java.util.function.Supplier;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.blocks.entity.AxolotlShelterBlockEntity;
import io.github.antthluca.axopearl.blocks.entity.EletricAnvilBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
        BuiltInRegistries.BLOCK_ENTITY_TYPE, Axopearl.MODID);

    // Block Entities
    public static final Supplier<BlockEntityType<AxolotlShelterBlockEntity>> AXOLOTL_SHELTER_BE = BLOCK_ENTITIES.register(
        "axolotl_shelter_be", () -> new BlockEntityType<>(
            AxolotlShelterBlockEntity::new,
            InitBlocks.AXOLOTL_SHELTER.get()));

    public static final Supplier<BlockEntityType<EletricAnvilBlockEntity>> ELETRIC_ANVIL_BE = BLOCK_ENTITIES.register(
        "eletric_anvil_be", () -> new BlockEntityType<>(
            EletricAnvilBlockEntity::new,
            InitBlocks.ELETRIC_ANVIL.get()));
}
