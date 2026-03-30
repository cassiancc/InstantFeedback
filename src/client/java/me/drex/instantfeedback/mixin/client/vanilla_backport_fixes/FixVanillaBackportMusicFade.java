package me.drex.instantfeedback.mixin.client.vanilla_backport_fixes;

import com.blackgear.vanillabackport.client.api.music.MusicFadeManager;
import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.mixin.access.SoundEngineAccessor;
import com.blackgear.vanillabackport.core.mixin.access.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MusicFadeManager.class)
public abstract class FixVanillaBackportMusicFade {

    @Shadow @Final private MusicManager manager;
    @Shadow private float currentGain;

    @Unique private static float persistentGain = -1.0F;
    @Unique private static boolean isFadingOut = false;

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private void productionFadeTakeover(SoundInstance currentMusic, CallbackInfoReturnable<Boolean> cir) {
        if (currentMusic == null) {
            persistentGain = -1.0F;
            isFadingOut = false;
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean inPaleGarden = mc.player.level().getBiome(mc.player.blockPosition()).is(ModBiomes.PALE_GARDEN);

        if (inPaleGarden) {
            if (!isFadingOut) {
                float startingVol = 1.0F;
                try {
                    if (mc.getSoundManager() != null) {
                        var engine = ((SoundManagerAccessor) mc.getSoundManager()).getSoundEngine();
                        if (engine != null) {
                            startingVol = this.currentGain * ((SoundEngineAccessor) engine).callCalculateVolume(currentMusic);
                        }
                    }
                } catch (Exception ignored) {}

                persistentGain = Math.min(startingVol, 1.0F);
                isFadingOut = true;
            }

            persistentGain = Math.max(0.0F, persistentGain - 0.025F);
            this.currentGain = persistentGain;

            // This sucks but since VanillaBackport is fighting me it's all I can do...
            try {
                var engine = ((SoundManagerAccessor) mc.getSoundManager()).getSoundEngine();
                var handle = ((SoundEngineAccessor) engine).getInstanceToChannel().get(currentMusic);
                if (handle != null) {
                    handle.execute(channel -> channel.setVolume(persistentGain));
                }
            } catch (Exception ignored) {}

            if (persistentGain <= 0.0F) {
                this.manager.stopPlaying();
                persistentGain = -1.0F;
                isFadingOut = false;
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(true);
            }
        } else {
            if (isFadingOut) {
                isFadingOut = false;
                persistentGain = -1.0F;
            }
        }
    }
}