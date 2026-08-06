package me.drex.instantfeedback.mixin.clock;

import me.drex.instantfeedback.config.ConfigManager;
import me.drex.instantfeedback.item.SavedTime;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void instantfeedback$onClockUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!ConfigManager.config().tinyTakeoverClockUtility) {
            return;
        }

        ItemStack itemStack = player.getItemInHand(hand);

        if (!itemStack.is(Items.CLOCK)) {
            return;
        }

        if (player.isShiftKeyDown()) {
            if (SavedTime.has(itemStack)) {
                SavedTime.remove(itemStack);
                cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide()));
            }
            return;
        }

        boolean replaceExistingStack = !player.getAbilities().instabuild && itemStack.getCount() == 1;

        float sunAngle = level.getTimeOfDay(1.0F) * 360.0F;
        SavedTime savedTime = new SavedTime(sunAngle);

        player.displayClientMessage(Component.translatable("item.instantfeedback.alarm_clock.set_time", savedTime.formatTime()), true);

        if (replaceExistingStack) {
            savedTime.writeToNbt(itemStack);
        } else {
            ItemStack savedTimeClock = new ItemStack(Items.CLOCK);
            itemStack.shrink(1);
            savedTime.writeToNbt(savedTimeClock);
            if (!player.getInventory().add(savedTimeClock)) {
                player.drop(savedTimeClock, false);
            }
        }

        cir.setReturnValue(InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide()));
    }
}