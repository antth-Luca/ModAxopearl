package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.blocks.AxolotlShelterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Axopearl.MODID);

    // Blocks
    public static final DeferredBlock<Block> AXOLOTL_SHELTER = BLOCKS.registerBlock(
        "axolotl_shelter", (properties) -> new AxolotlShelterBlock(properties
                .mapColor(MapColor.TERRACOTTA_RED)
                .instrument(NoteBlockInstrument.BASS)
                .strength(0.6F)
                .sound(SoundType.STONE)));
}
