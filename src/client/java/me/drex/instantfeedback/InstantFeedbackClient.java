package me.drex.instantfeedback;

import me.drex.instantfeedback.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import me.drex.instantfeedback.particle.CreakingEyesParticle;
import net.minecraft.client.renderer.RenderType;

public class InstantFeedbackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(InstantFeedback.CREAKING_EYES, CreakingEyesParticle.Provider::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.CARVED_PALE_PUMPKIN,
                ModBlocks.PALE_ROSE,
                ModBlocks.PALE_BUSH,
                ModBlocks.TALL_PALE_BUSH,
                ModBlocks.POTTED_PALE_ROSE,
//                ModBlocks.POTTED_CACTUS_FLOWER,
                ModBlocks.POTTED_ROSE_BUSH,
                ModBlocks.POTTED_PEONY,
                ModBlocks.POTTED_LILAC,
                ModBlocks.POTTED_SUNFLOWER,
                ModBlocks.POTTED_PITCHER_PLANT);
    }
}