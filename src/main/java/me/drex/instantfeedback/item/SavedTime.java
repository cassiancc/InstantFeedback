package me.drex.instantfeedback.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record SavedTime(float sunAngle) {

    public void writeToNbt(ItemStack stack) {
        stack.getOrCreateTag().putFloat("SavedSunAngle", sunAngle);
    }

    public static SavedTime readFromNbt(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("SavedSunAngle")) {
            return new SavedTime(tag.getFloat("SavedSunAngle"));
        }
        return null;
    }

    public static boolean has(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("SavedSunAngle");
    }

    public static void remove(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove("SavedSunAngle");
            if (tag.isEmpty()) {
                stack.setTag(null);
            }
        }
    }

    public Component formatTime() {
        float approxHour = (((sunAngle / 360.0f) * 24.0f) + 12.0f) % 24.0f;
        int hour = (int) Math.floor(approxHour);
        int minute = (int) Math.floor((approxHour - hour) * 60.0f);
        return Component.literal(String.format("%02d:%02d", hour, minute));
    }
}