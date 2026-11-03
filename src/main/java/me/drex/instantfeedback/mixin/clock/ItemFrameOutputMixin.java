package me.drex.instantfeedback.mixin.clock;

import me.drex.instantfeedback.item.SavedTime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameOutputMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "getAnalogOutput", at = @At("RETURN"), cancellable = true)
    private void instantfeedback$getAnalogOutput(CallbackInfoReturnable<Integer> cir) {
        ItemStack item = getItem();
        if (SavedTime.has(item)) {
            CompoundTag tag = item.getTag();
            if (tag != null && tag.getBoolean("active")) {
                cir.setReturnValue(15);
            } else {
                cir.setReturnValue(0);
            }
        }
    }
}