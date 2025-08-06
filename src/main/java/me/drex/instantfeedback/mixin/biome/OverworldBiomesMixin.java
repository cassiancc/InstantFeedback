package me.drex.instantfeedback.mixin.biome;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.biomes.TheGardenAwakensBiomes;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.instantfeedback.InstantFeedback;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TheGardenAwakensBiomes.class)
public class OverworldBiomesMixin {

    @WrapWithCondition(
        method = "paleGarden",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/data/worldgen/BiomeDefaultFeatures;commonSpawns(Lnet/minecraft/world/level/biome/MobSpawnSettings$Builder;)V"
        )
    )
    private static boolean disablePaleGardenMonsterSpawning(MobSpawnSettings.Builder builder) {
        return false;
    }

}
