package me.drex.instantfeedback.entity;

import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModPigVariants {
    public static final ResourceKey<Registry<PigVariant>> PIG_VARIANT_REGISTRY =
            ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "pig_variant"));

    public static final ResourceKey<PigVariant> MUDDY =
            ResourceKey.create(PIG_VARIANT_REGISTRY, new ResourceLocation("instantfeedback", "muddy"));
}