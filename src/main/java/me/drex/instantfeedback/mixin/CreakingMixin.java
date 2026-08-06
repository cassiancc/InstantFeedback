package me.drex.instantfeedback.mixin;

import me.drex.instantfeedback.config.ConfigManager;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creaking.class)
public abstract class CreakingMixin {

    @Inject(
            method = "createAttributes",
            at = @At("RETURN")
    )
    private static void increaseMovementSpeed(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        if (ConfigManager.config().theGardenAwakensBuffCreaking) {
            cir.getReturnValue().add(Attributes.MOVEMENT_SPEED, 0.45);
        }
    }

    @Inject(
            method = "doHurtTarget",
            at = @At("HEAD")
    )
    private void instantfeedback$applyDifficultyDamage(net.minecraft.world.entity.Entity target, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.config().theGardenAwakensBuffCreaking) {
            float scaledDamage = switch (target.level().getDifficulty()) {
                case EASY -> 9.0F; // Minecraft will reduce this to 6
                case NORMAL -> 10.0F;
                case HARD -> 20.0F / 1.5F; // Minecraft will scale this to 20
                default -> 10.0F;
            };

            ((net.minecraft.world.entity.LivingEntity)(Object)this)
                    .getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
                    .setBaseValue(scaledDamage);
        }
    }
}