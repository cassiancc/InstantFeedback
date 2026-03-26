package me.drex.instantfeedback.mixin.call_happy_ghast;

import me.drex.instantfeedback.config.ConfigManager;
import me.drex.instantfeedback.duck.IHappyGhast;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InstrumentItem.class)
public abstract class InstrumentItemMixin {

    @Unique
    private static Class<? extends Entity> instantfeedback$happyGhastClass;
    @Unique
    private static boolean instantfeedback$checkedForMod = false;

    @Inject(method = "play", at = @At("TAIL"))
    private static void callGhasts(Level level, Player player, Instrument instrument, CallbackInfo ci) {
        if (!ConfigManager.config().chaseTheSkiesHappyGhastCalling) return;

        if (!instantfeedback$checkedForMod) {
            try {
                Class<?> clazz = Class.forName("com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast");
                if (Entity.class.isAssignableFrom(clazz)) {
                    instantfeedback$happyGhastClass = (Class<? extends Entity>) clazz;
                }
            } catch (ClassNotFoundException ignored) {}
            instantfeedback$checkedForMod = true;
        }
        if (instantfeedback$happyGhastClass != null) {
            for (Entity entity : level.getEntitiesOfClass(instantfeedback$happyGhastClass, player.getBoundingBox().inflate(64))) {
                if (entity instanceof IHappyGhast happyGhast) {
                    happyGhast.instantfeedback$setCallerPosition(player.position());
                }
            }
        }
    }
}
