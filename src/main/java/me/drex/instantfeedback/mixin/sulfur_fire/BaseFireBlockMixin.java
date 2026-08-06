package me.drex.instantfeedback.mixin.sulfur_fire;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.drex.instantfeedback.block.ModBlocks;
import me.drex.instantfeedback.block.SulfurFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @ModifyReturnValue(method = "getState", at = @At("RETURN"))
    private static BlockState sulfurFire(BlockState original, BlockGetter level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        if (SulfurFireBlock.canSurviveOnBlock(belowState)) {
            return ModBlocks.SULFUR_FIRE.defaultBlockState();
        }
        return original;
    }
}