package io.github.antthluca.axopearl.utils;

import io.github.antthluca.axopearl.Axopearl;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class AxopearlTags {
    public static class EntityTypeTags {
        public static TagKey<EntityType<?>> AXOLOTL_SHELTER_INHABITORS = create("axolotl_shelter_inhabitors");

        private static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, name));
        }
    }

    public static class BlockTags {
        public static TagKey<Block> AXOLOTL_SHELTER = create("axolotl_shelter");

        private static TagKey<Block> create(String name) {
            return net.minecraft.tags.BlockTags.create(ResourceLocation.fromNamespaceAndPath(Axopearl.MODID, name));
        }
    }
}
