package me.drex.instantfeedback.mixin.snow_golem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public class SnowballMixin {

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void instantfeedback$applyFireDamageOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Snowball snowball = (Snowball) (Object) this;

        if (snowball.isOnFire()) {
            Entity target = entityHitResult.getEntity();

            target.setSecondsOnFire(1);
        }
    }
}