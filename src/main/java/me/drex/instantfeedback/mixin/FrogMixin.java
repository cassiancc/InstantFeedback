package me.drex.instantfeedback.mixin;

import me.drex.instantfeedback.block.ModBiomeTags;
import me.drex.instantfeedback.entity.ModFrogVariants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frog.class)
public class FrogMixin {
    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/FrogAi;initMemories(Lnet/minecraft/world/entity/animal/frog/Frog;Lnet/minecraft/util/RandomSource;)V"))
    private void addDarkFrog(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        Frog that = (Frog) (Object) this;
        Holder<Biome> holder = serverLevelAccessor.getBiome(that.blockPosition());
        if (holder.is(ModBiomeTags.IS_DARK_FOREST)) {
            that.setVariant(BuiltInRegistries.FROG_VARIANT.getHolderOrThrow(ModFrogVariants.DARK));
        }
    }
}