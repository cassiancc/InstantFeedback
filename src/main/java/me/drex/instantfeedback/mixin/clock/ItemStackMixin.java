package me.drex.instantfeedback.mixin.clock;

import me.drex.instantfeedback.item.SavedTime;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
    private void instantfeedback$addAlarmGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.is(Items.CLOCK) && SavedTime.has(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    private void instantfeedback$customAlarmName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.is(Items.CLOCK) && SavedTime.has(stack)) {
            cir.setReturnValue(Component.translatable("item.instantfeedback.saved_time_clock"));
        }
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void instantfeedback$addAlarmTooltip(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.is(Items.CLOCK) && SavedTime.has(stack)) {
            SavedTime savedTime = SavedTime.readFromNbt(stack);
            if (savedTime != null) {
                cir.getReturnValue().add(Component.translatable("item.instantfeedback.alarm_clock.saved_time", savedTime.formatTime()).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}