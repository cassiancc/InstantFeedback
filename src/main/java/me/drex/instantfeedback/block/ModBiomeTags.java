package me.drex.instantfeedback.block;

import me.drex.instantfeedback.InstantFeedback;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModBiomeTags {
    public static final TagKey<Biome> IS_DARK_FOREST = create("is_dark_forest");

    private ModBiomeTags() {
    }


    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", string));
    }
}
