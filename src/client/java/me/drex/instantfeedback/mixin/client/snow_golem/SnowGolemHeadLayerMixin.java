package me.drex.instantfeedback.mixin.client.snow_golem;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.instantfeedback.block.ModBlocks;
import me.drex.instantfeedback.duck.ISnowGolem;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(SnowGolemHeadLayer.class)
public abstract class SnowGolemHeadLayerMixin {

    @WrapOperation(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/SnowGolem;hasPumpkin()Z"
            )
    )
    public boolean instantfeedback$orPalePumpkin(SnowGolem instance, Operation<Boolean> original) {
        return original.call(instance) || ((ISnowGolem) instance).instantfeedback$hasPalePumpkin();
    }

    @WrapOperation(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/SnowGolem;FFFFFF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/block/Blocks;CARVED_PUMPKIN:Lnet/minecraft/world/level/block/Block;"
            )
    )
    private Block instantfeedback$renderPalePumpkin(Operation<Block> original, PoseStack poseStack, MultiBufferSource buffer, int packedLight, SnowGolem snowGolem) {
        if (((ISnowGolem) snowGolem).instantfeedback$hasPalePumpkin()) {
            return ModBlocks.CARVED_PALE_PUMPKIN;
        }
        return original.call();
    }

}
