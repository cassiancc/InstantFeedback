package me.drex.instantfeedback.mixin.clock;

import me.drex.instantfeedback.item.SavedTime;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {

    @Shadow
    public abstract ItemStack getItem();

    @Unique
    private float instantfeedback$lastSunAngle = -1;

    @Unique
    private int instantfeedback$activeTicks = 0;

    protected ItemFrameMixin(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (instantfeedback$activeTicks > 0) {
                this.level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
                instantfeedback$activeTicks--;
            }

            ItemStack item = getItem();
            SavedTime savedTime = SavedTime.readFromNbt(item);

            if (savedTime != null) {
                float targetAngle = savedTime.sunAngle();
                float currentAngle = this.level().getTimeOfDay(1.0F) * 360.0F;

                if (instantfeedback$lastSunAngle >= 0) {
                    float delta = (currentAngle - instantfeedback$lastSunAngle + 360.0F) % 360.0F;
                    float targetDelta = (targetAngle - instantfeedback$lastSunAngle + 360.0F) % 360.0F;

                    if (targetDelta > 0.0F && targetDelta <= delta) {
                        instantfeedback$activeTicks = 2;
                    }
                }
                instantfeedback$lastSunAngle = currentAngle;
                this.level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
            } else {
                instantfeedback$lastSunAngle = -1;
            }
        }

        super.tick();
    }

    @Inject(method = "getAnalogOutput", at = @At("RETURN"), cancellable = true)
    private void instantfeedback$getAnalogOutput(CallbackInfoReturnable<Integer> cir) {
        ItemStack item = getItem();
        if (SavedTime.has(item)) {
            if (instantfeedback$activeTicks > 0) {
                cir.setReturnValue(15);
            } else {
                cir.setReturnValue(0);
            }
        }
    }
}