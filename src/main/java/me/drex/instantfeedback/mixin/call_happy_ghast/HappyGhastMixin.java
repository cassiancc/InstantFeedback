package me.drex.instantfeedback.mixin.call_happy_ghast;

import me.drex.instantfeedback.duck.IHappyGhast;
import net.minecraft.world.phys.Vec3;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HappyGhast.class)
public abstract class HappyGhastMixin implements IHappyGhast {

    @Unique
    private Vec3 instantfeedback$callerPosition;

    @Override
    public void instantfeedback$setCallerPosition(Vec3 instantfeedback$callerPosition) {
        this.instantfeedback$callerPosition = instantfeedback$callerPosition;
    }

    @Override
    public @Nullable Vec3 instantfeedback$getCallerPosition() {
        return instantfeedback$callerPosition;
    }
}
