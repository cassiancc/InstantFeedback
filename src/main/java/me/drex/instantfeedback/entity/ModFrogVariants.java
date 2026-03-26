package me.drex.instantfeedback.entity;

import com.blackgear.vanillabackport.common.level.entities.animal.FrogDataVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModFrogVariants {
    public static final ResourceKey<Registry<FrogDataVariant>> FROG_VARIANT_REGISTRY =
            ResourceKey.createRegistryKey(new ResourceLocation("vanillabackport", "frog_variant"));

    public static final ResourceKey<FrogDataVariant> DARK =
            ResourceKey.create(FROG_VARIANT_REGISTRY, new ResourceLocation("instantfeedback", "dark"));
}