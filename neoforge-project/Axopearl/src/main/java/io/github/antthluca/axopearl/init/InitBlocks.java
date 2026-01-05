package io.github.antthluca.axopearl.init;

import io.github.antthluca.axopearl.Axopearl;
import io.github.antthluca.axopearl.blocks.AxolotlShelterBlock;
import io.github.antthluca.axopearl.blocks.EletricAnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Axopearl.MODID);

    // Blocks
    public static final DeferredBlock<Block> AXOLOTL_SHELTER = BLOCKS.registerBlock(
        "axolotl_shelter", (properties) -> new AxolotlShelterBlock(properties
            .mapColor(MapColor.COLOR_RED)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)
            .instrument(NoteBlockInstrument.BASEDRUM)
        ));

    public static final DeferredBlock<Block> ELETRIC_ANVIL = BLOCKS.registerBlock(
        "eletric_anvil", (properties) -> new EletricAnvilBlock(properties
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 1200.0F)
            .sound(SoundType.ANVIL)
            .pushReaction(PushReaction.BLOCK)
        ));
}
