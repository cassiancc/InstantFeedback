package me.drex.instantfeedback.mixin.client;

import com.blackgear.vanillabackport.common.level.blocks.HangingMossBlock;
import me.drex.instantfeedback.InstantFeedback;
import me.drex.instantfeedback.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static com.blackgear.vanillabackport.common.registries.ModBiomes.PALE_GARDEN;

@Mixin(value = HangingMossBlock.class)
public abstract class HangingMossBlockMixin {

    @Inject(
            method = "animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At("HEAD"),
            require = 0
    )
    public void addParticle(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {

        if (!level.getBiome(blockPos).is(PALE_GARDEN)) return;

        double maxHorizontalDistSq = 49.0;

        for (net.minecraft.world.entity.player.Player player : level.players()) {
            double dx = (blockPos.getX() + 0.5) - player.getX();
            double dz = (blockPos.getZ() + 0.5) - player.getZ();
            double dy = (blockPos.getY() + 0.5) - player.getY();

            if ((dx * dx + dz * dz) <= maxHorizontalDistSq && Math.abs(dy) < 15.0) {
                return;
            }
        }

        long time = level.getDayTime() % 24000;
        boolean isNight = time >= 13000 && time <= 23000;

        int blockLight = level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK, blockPos);

        if (ConfigManager.config().theGardenAwakensAmbientParticles && isNight && blockLight < 4 && randomSource.nextInt(400) == 0) {

            for (Map.Entry<Property<?>, Comparable<?>> entry : blockState.getValues().entrySet()) {
                Property<?> property = entry.getKey();

                if (property.getName().equals("tip")) {
                    if (entry.getValue().toString().equalsIgnoreCase("true")) {
                        level.addParticle(
                                InstantFeedback.CREAKING_EYES,
                                blockPos.getX() + 0.5,
                                blockPos.getY() + 0.5,
                                blockPos.getZ() + 0.5,
                                0.0,
                                0.0,
                                0.0
                        );
                    }
                    break;
                }
            }
        }
    }
}