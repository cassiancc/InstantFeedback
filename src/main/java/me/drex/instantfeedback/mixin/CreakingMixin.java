package me.drex.instantfeedback.mixin;

import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import me.drex.instantfeedback.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Creaking.class)
public abstract class CreakingMixin {

    @ModifyConstant(
        method = "createAttributes",
        constant = @Constant(doubleValue = 0.4)
    )
    private static double increaseMovementSpeed(double original) {
        return ConfigManager.config().theGardenAwakensBuffCreaking ? 0.45F : original;
    }

    @ModifyConstant(
        method = "createAttributes",
        constant = @Constant(doubleValue = 3.0)
    )
    private static double increaseAttackDamage(double original) {
        return ConfigManager.config().theGardenAwakensBuffCreaking ? 10 : original;
    }
}
