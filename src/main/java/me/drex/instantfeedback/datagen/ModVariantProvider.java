package me.drex.instantfeedback.datagen;

import com.blackgear.vanillabackport.common.level.entities.animal.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import java.util.concurrent.CompletableFuture;


// For some reason the data generation for the pig and frog variants is broken. But whatever we ball
public class ModVariantProvider implements DataProvider {
    protected final FabricDataOutput output;
    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public ModVariantProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        this.output = output;
        this.registryLookup = registryLookup;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registryLookup.thenCompose(registries -> {
            var biomeLookup = registries.lookupOrThrow(net.minecraft.core.registries.Registries.BIOME);

            PigVariant muddyPig = new PigVariant(
                    new com.blackgear.vanillabackport.common.api.variant.ModelAndTexture<>(
                            PigVariant.ModelType.NORMAL,
                            new ResourceLocation("instantfeedback", "entity/pig/muddy_pig")
                    ),
                    com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors.single(
                            new com.blackgear.vanillabackport.common.api.variant.spawn.check.BiomeCheck(
                                    net.minecraft.core.HolderSet.direct(biomeLookup.getOrThrow(net.minecraft.world.level.biome.Biomes.SWAMP))
                            ), 1)
            );

            FrogDataVariant darkFrog = new FrogDataVariant(
                    new com.blackgear.vanillabackport.common.api.variant.ClientAsset(
                            new ResourceLocation("instantfeedback", "entity/frog/dark_frog")
                    ),
                    com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors.single(
                            new com.blackgear.vanillabackport.common.api.variant.spawn.check.BiomeCheck(
                                    net.minecraft.core.HolderSet.direct(
                                            biomeLookup.getOrThrow(net.minecraft.world.level.biome.Biomes.DARK_FOREST),
                                            biomeLookup.getOrThrow(net.minecraft.world.level.biome.Biomes.DEEP_DARK)
                                    )
                            ), 1)
            );

            return CompletableFuture.allOf(
                    saveVariant(writer, registries, PigVariant.CODEC, muddyPig, "pig_variant/muddy"),
                    saveVariant(writer, registries, FrogDataVariant.CODEC, darkFrog, "frog_variant/dark")
            );
        });
    }

    private <T> CompletableFuture<?> saveVariant(CachedOutput writer, HolderLookup.Provider registries, com.mojang.serialization.Codec<T> codec, T value, String path) {
        var ops = net.minecraft.resources.RegistryOps.create(com.mojang.serialization.JsonOps.INSTANCE, registries);
        com.google.gson.JsonElement json = codec.encodeStart(ops, value).getOrThrow(false, System.err::println);

        var resourcePath = this.output.createPathProvider(net.minecraft.data.PackOutput.Target.DATA_PACK, "vanillabackport").json(new ResourceLocation(path));

        return DataProvider.saveStable(writer, json, resourcePath);
    }

    @Override
    public String getName() {
        return "InstantFeedback Variants";
    }
}