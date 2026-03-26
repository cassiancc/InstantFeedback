package me.drex.instantfeedback;

import com.blackgear.vanillabackport.core.fabric.emissive.EmissiveModelWrapper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class InstantFeedbackEmissiveHandler implements ClientModInitializer {
// I cast 100 years of health and good fortune upon ItsBlackGear
    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(pluginContext -> {
            ResourceLocation emissiveModelId = new ResourceLocation("instantfeedback", "block/carved_pale_pumpkin_eyes_emissive");
            pluginContext.addModels(emissiveModelId);

            pluginContext.modifyModelAfterBake().register((model, context) -> {
                ResourceLocation id = context.id();
                if (id == null || !id.getNamespace().equals("instantfeedback")) return model;

                String idString = id.toString();

                if (idString.equals("instantfeedback:carved_pale_pumpkin#inventory")) {
                    BakedModel emissiveModel = context.baker().bake(emissiveModelId, net.minecraft.client.resources.model.BlockModelRotation.X0_Y0);
                    return (emissiveModel != null) ? new EmissiveModelWrapper(model, emissiveModel) : model;
                }

                if (idString.contains("carved_pale_pumpkin")) {
                    net.minecraft.client.resources.model.ModelState rotation = null;

                    if (idString.endsWith("#facing=north")) rotation = net.minecraft.client.resources.model.BlockModelRotation.X0_Y0;
                    else if (idString.endsWith("#facing=east"))  rotation = net.minecraft.client.resources.model.BlockModelRotation.X0_Y90;
                    else if (idString.endsWith("#facing=south")) rotation = net.minecraft.client.resources.model.BlockModelRotation.X0_Y180;
                    else if (idString.endsWith("#facing=west"))  rotation = net.minecraft.client.resources.model.BlockModelRotation.X0_Y270;

                    if (rotation != null) {
                        BakedModel emissiveModel = context.baker().bake(emissiveModelId, rotation);
                        if (emissiveModel != null) {
                            return new EmissiveModelWrapper(model, emissiveModel);
                        }
                    }
                }

                return model;
            });
        });
    }
}