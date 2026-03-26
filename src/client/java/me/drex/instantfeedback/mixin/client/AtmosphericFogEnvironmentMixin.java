package me.drex.instantfeedback.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.drex.instantfeedback.config.ConfigManager;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class AtmosphericFogEnvironmentMixin {

    @Unique private static float nightMultiplier = 1.0f;
    @Unique private static float targetBiomeWeight = 0.0f;
    @Unique private static float currentFogIntensity = 0.0f;

    @Inject(method = "setupFog", at = @At("HEAD"))
    private static void captureContext(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickFog, float partialTick, CallbackInfo ci) {
        if (camera.getEntity().level() instanceof ClientLevel level) {
            int ix = (int) Math.floor(camera.getPosition().x);
            int iz = (int) Math.floor(camera.getPosition().z);
            int iy = (int) camera.getPosition().y;

            float totalWeight = 0.0f;
            int range = 8;

            BlockPos[] samples = {
                    new BlockPos(ix, iy, iz),
                    new BlockPos(ix + range, iy, iz), new BlockPos(ix - range, iy, iz),
                    new BlockPos(ix, iy, iz + range), new BlockPos(ix, iy, iz - range),
                    new BlockPos(ix + range, iy, iz + range),
                    new BlockPos(ix - range, iy, iz - range),
                    new BlockPos(ix + range, iy, iz - range),
                    new BlockPos(ix - range, iy, iz + range)
            };

            for (BlockPos p : samples) {
                if (level.getBiome(p).unwrapKey().map(key -> key.location().toString().equals("minecraft:pale_garden")).orElse(false)) {
                    totalWeight += 1.0f;
                }
            }

            float rawWeight = totalWeight / 9.0f;

            boolean isDirectlyInBiome = level.getBiome(new BlockPos(ix, iy, iz)).unwrapKey()
                    .map(key -> key.location().toString().equals("minecraft:pale_garden")).orElse(false);

            if (isDirectlyInBiome && rawWeight < 1.0f) {
                rawWeight = Math.min(1.0f, rawWeight + 0.5f);
            }

            targetBiomeWeight = rawWeight;

            float interpolationSpeed = 0.03f;
            currentFogIntensity = lerp(interpolationSpeed, currentFogIntensity, targetBiomeWeight);

            if (currentFogIntensity > 0 && ConfigManager.config().theGardenAwakensFog) {
                float dayTime = level.getDayTime() % 24000;
                float cos = (float) Math.cos(((dayTime - 18000) / 24000f) * Math.PI * 2);
                nightMultiplier = (Math.max(0, cos) * 3.0f) + 1.0f;
            } else {
                nightMultiplier = 1.0f;
            }
        }
    }

    @Redirect(
            method = "setupFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V")
    )
    private static void redirectFogStart(float start) {
        if (currentFogIntensity > 0.001f && ConfigManager.config().theGardenAwakensFog) {
            RenderSystem.setShaderFogStart(lerp(currentFogIntensity, start, -10.0f));
        } else {
            RenderSystem.setShaderFogStart(start);
        }
    }

    @Redirect(
            method = "setupFog",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V")
    )
    private static void redirectFogEnd(float end) {
        if (currentFogIntensity > 0.001f && ConfigManager.config().theGardenAwakensFog) {
            float targetEnd = (end / 2.5f) / nightMultiplier;
            RenderSystem.setShaderFogEnd(lerp(currentFogIntensity, end, targetEnd));
        } else {
            RenderSystem.setShaderFogEnd(end);
        }
    }

    @Unique
    private static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }
}