package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.blocks.AxolotlShelterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Axopearl.MODID);

    // Blocks
    public static final DeferredBlock<Block> AXOLOTL_SHELTER = BLOCKS.register(
        "axolotl_shelter", () -> new AxolotlShelterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
}
