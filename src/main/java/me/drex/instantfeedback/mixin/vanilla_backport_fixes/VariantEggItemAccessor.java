package me.drex.instantfeedback.mixin.vanilla_backport_fixes;

import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.items.VariantEggItem;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VariantEggItem.class)
public interface VariantEggItemAccessor {
    @Accessor("variant")
    ResourceKey<ChickenVariant> getVariant();
}