package me.drex.instantfeedback.mixin.clock;

import me.drex.instantfeedback.item.SavedTime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingEntity.class)
public abstract class ItemFrameMixin {

    @Unique
    private float instantfeedback$lastSunAngle = -1;

    @Unique
    private int instantfeedback$activeTicks = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void instantfeedback$onTick(CallbackInfo ci) {
        if ((Object) this instanceof ItemFrame itemFrame) {
            Level level = itemFrame.level();

            if (!level.isClientSide()) {
                ItemStack item = itemFrame.getItem();
                CompoundTag tag = item.getTag();

                if (instantfeedback$activeTicks > 0) {
                    level.updateNeighbourForOutputSignal(itemFrame.blockPosition(), Blocks.AIR);
                    instantfeedback$activeTicks--;
                    if (instantfeedback$activeTicks == 0 && tag != null) {
                        tag.putBoolean("active", false);
                    }
                }

                SavedTime savedTime = SavedTime.readFromNbt(item);

                if (savedTime != null) {
                    float targetAngle = savedTime.sunAngle();
                    float currentAngle = level.getTimeOfDay(1.0F) * 360.0F;

                    if (instantfeedback$lastSunAngle >= 0) {
                        float delta = (currentAngle - instantfeedback$lastSunAngle + 360.0F) % 360.0F;
                        float targetDelta = (targetAngle - instantfeedback$lastSunAngle + 360.0F) % 360.0F;

                        if (targetDelta > 0.0F && targetDelta <= delta) {
                            instantfeedback$activeTicks = 2;
                            item.getOrCreateTag().putBoolean("active", true);
                        }
                    }
                    instantfeedback$lastSunAngle = currentAngle;
                    level.updateNeighbourForOutputSignal(itemFrame.blockPosition(), Blocks.AIR);
                } else {
                    instantfeedback$lastSunAngle = -1;
                    if (tag != null) {
                        tag.remove("active");
                    }
                }
            }
        }
    }
}