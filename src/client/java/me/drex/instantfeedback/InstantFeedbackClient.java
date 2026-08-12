package me.drex.instantfeedback;

import me.drex.instantfeedback.block.ModBlocks;
import me.drex.instantfeedback.item.SavedTime;
import me.drex.instantfeedback.particle.CreakingEyesParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class InstantFeedbackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(InstantFeedback.CREAKING_EYES, CreakingEyesParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(InstantFeedback.SULFUR_FLAME, FlameParticle.Provider::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.CARVED_PALE_PUMPKIN,
                ModBlocks.PALE_ROSE,
                ModBlocks.PALE_BUSH,
                ModBlocks.TALL_PALE_BUSH,
                ModBlocks.POTTED_PALE_ROSE,
                ModBlocks.POTTED_PALE_BUSH,
                ModBlocks.POTTED_TALL_PALE_BUSH,
                ModBlocks.POTTED_CACTUS_FLOWER,
                ModBlocks.POTTED_ROSE_BUSH,
                ModBlocks.POTTED_PEONY,
                ModBlocks.POTTED_LILAC,
                ModBlocks.POTTED_SUNFLOWER,
                ModBlocks.POTTED_PITCHER_PLANT,
                ModBlocks.SULFUR_FIRE,
                ModBlocks.SULFUR_TORCH,
                ModBlocks.SULFUR_WALL_TORCH,
                ModBlocks.SULFUR_LANTERN,
                ModBlocks.SULFUR_CAMPFIRE,
                ModBlocks.GLOWING_VINES
        );

        ItemProperties.register(Items.CLOCK, new ResourceLocation("time"), (stack, level, entity, seed) -> {
            SavedTime savedTime = SavedTime.readFromNbt(stack);
            if (savedTime != null) {
                return savedTime.sunAngle() / 360.0F;
            }

            boolean hasEntity = entity != null;
            net.minecraft.world.entity.Entity currentEntity = hasEntity ? entity : stack.getEntityRepresentation();
            if (level == null && currentEntity != null && currentEntity.level() instanceof net.minecraft.client.multiplayer.ClientLevel) {
                level = (net.minecraft.client.multiplayer.ClientLevel) currentEntity.level();
            }
            if (level == null) {
                return 0.0F;
            }

            double rotation;
            if (level.dimensionType().natural()) {
                rotation = level.getTimeOfDay(1.0F);
            } else {
                rotation = Math.random();
            }
            return (float) rotation;
        });
    }
}