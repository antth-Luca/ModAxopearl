package io.github.antthluca.axopearl.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class AxopearlTags {
    public static class EntityTypeTags {
        public static TagKey<EntityType<?>> AXOLOTL_SHELTER_INHABITORS = create("axolotl_shelter_inhabitors");

        private static TagKey<EntityType<?>> create(String name) {
            return EntityTypeTags.create(name);
        }
    }
}
