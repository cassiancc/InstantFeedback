package me.drex.instantfeedback.mixin;

import me.drex.instantfeedback.InstantFeedback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GrassBlock.class, priority = 2000)
public class PaleGardenBonemealMixin {

    @Inject(method = "performBonemeal", at = @At("HEAD"))
    private void instantfeedback$dispenserSupport(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
        InstantFeedback.PaleBonemealGrowth.grow(level, pos, random);
    }
}