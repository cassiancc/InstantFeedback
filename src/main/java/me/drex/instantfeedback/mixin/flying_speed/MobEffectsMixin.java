package me.drex.instantfeedback.mixin.flying_speed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.instantfeedback.config.ConfigManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEffects.class)
public abstract class MobEffectsMixin {
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifier(Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/lang/String;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)Lnet/minecraft/world/effect/MobEffect;"
            )
    ) private static MobEffect addFlyingSpeedAttributeModifier(MobEffect instance, Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation, Operation<MobEffect> original) {
        MobEffect effect = original.call(instance, attribute, uuid, amount, operation);

        if (ConfigManager.config().chaseTheSkiesHappyGhastSpeed && attribute == Attributes.MOVEMENT_SPEED) {
            effect.addAttributeModifier(Attributes.FLYING_SPEED, uuid, amount, operation);
        }

        return effect;
    }
}
