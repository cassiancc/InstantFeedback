package me.drex.instantfeedback.mixin.call_happy_ghast;

import me.drex.instantfeedback.duck.IHappyGhast;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast$GhastMoveControl")
public abstract class GhastMoveControlMixin {
    @Shadow(remap = false)
    @Final
    private com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast ghast;

    @Shadow(remap = false)
    protected abstract boolean canReach(Vec3 vec3);

    @Shadow(remap = false)
    private int floatDuration;

    @Unique
    private int instantfeedback$callerTicks = -1;

    @Inject(
        method = "tick",
        at = @At(
            value = "FIELD",
            target = "Lcom/blackgear/vanillabackport/common/level/entities/happyghast/HappyGhast$GhastMoveControl;floatDuration:I",
            ordinal = 0,
            opcode = Opcodes.GETFIELD
        ),
           cancellable = true,
           remap = false
    )
    public void moveToCaller(CallbackInfo ci) {
        if ((Object)this.ghast instanceof IHappyGhast happyGhast) {
            if (instantfeedback$callerTicks == -1) {
                instantfeedback$callerTicks = this.ghast.getRandom().nextInt(100) + 100;
            }


            Vec3 callerPosition = happyGhast.instantfeedback$getCallerPosition();
            if (callerPosition != null) {
                Vec3 position = this.ghast.position();

                Vec3 targetDelta = callerPosition.subtract(position);

                if (canReach(targetDelta.normalize())) {
                    if (targetDelta.lengthSqr() > 3 && instantfeedback$callerTicks > 0) {
                        double speed = this.ghast.getAttributeValue(Attributes.FLYING_SPEED);
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(targetDelta.normalize().scale(speed / 3.0)));

                        instantfeedback$callerTicks--;
                        ci.cancel();
                        return;
                    } else {
                        this.floatDuration = this.ghast.getRandom().nextInt(50) + 50;
                    }
                }
                instantfeedback$callerTicks = -1;
                happyGhast.instantfeedback$setCallerPosition(null);
            }
        }
    }
}
