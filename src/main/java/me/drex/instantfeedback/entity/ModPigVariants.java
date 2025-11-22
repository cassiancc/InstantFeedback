package me.drex.instantfeedback.entity;

import com.blackgear.vanillabackport.common.api.variant.BiomeCheck;
import com.blackgear.vanillabackport.common.api.variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.variant.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import me.drex.instantfeedback.InstantFeedback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModPigVariants {
    public static final ResourceKey<PigVariant> MUDDY = ResourceKey.create(ModRegistries.PIG_VARIANT_KEY, InstantFeedback.id("muddy"));

    public static void bootstrap(RegistryAccess access) {
        register(access, "muddy", PigVariant.ModelType.NORMAL, "muddy_pig", ConventionalBiomeTags.IS_SWAMP);
    }

    private static void register(RegistryAccess access, String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        access.lookup(Registries.BIOME).ifPresent(lookup -> register(key, type, assetId, SpawnPrioritySelectors.single(new BiomeCheck(lookup.getOrThrow(biome)), 1)));
    }

    private static void register(String key, PigVariant.ModelType type, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation path = InstantFeedback.id("entity/pig/" + assetId);
        ModBuiltinRegistries.PIG_VARIANTS.resource(key, new PigVariant(new ModelAndTexture<>(type, path), selectors));
    }
}