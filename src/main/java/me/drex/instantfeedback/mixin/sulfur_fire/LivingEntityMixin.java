package me.drex.instantfeedback.mixin.sulfur_fire;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "eat",
            at = @At("HEAD")
    )
    private void applySulfurNausea(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!level.isClientSide && stack.hasTag() && stack.getTag().getBoolean("SulfurCooked")) {
            LivingEntity entity = (LivingEntity) (Object) this;
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 0));
        }
    }
}