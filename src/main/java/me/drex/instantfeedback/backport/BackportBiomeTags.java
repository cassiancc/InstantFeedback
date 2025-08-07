package me.drex.instantfeedback.backport;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BackportBiomeTags {
    public static TagKey<Biome> IS_DARK_FOREST = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", "is_dark_forest"));
}
