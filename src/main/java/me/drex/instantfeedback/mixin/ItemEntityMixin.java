package me.drex.instantfeedback.mixin;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import me.drex.instantfeedback.block.ModBlocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            at = @At("RETURN")
    )
    private void instantfeedback$transformFroglightOnSpawn(Level world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        if (stack.is(Items.OCHRE_FROGLIGHT) || stack.is(Items.PEARLESCENT_FROGLIGHT) || stack.is(Items.VERDANT_FROGLIGHT)) {
            List<Frog> frogs = world.getEntitiesOfClass(Frog.class, new AABB(x - 1.5, y - 1.5, z - 1.5, x + 1.5, y + 1.5, z + 1.5));

            for (Frog frog : frogs) {
                if (isDarkFrog(frog)) {
                    stack.setTag(null);
                    ((ItemEntity)(Object)this).setItem(new ItemStack(ModBlocks.CERULEAN_FROGLIGHT, stack.getCount()));
                    break;
                }
            }
        }
    }

    @Unique
    private boolean isDarkFrog(Frog frog) {
        if ((Object)frog instanceof VariantDataHolder<?> holder) {
            return holder.getVariantData().map(v -> v.toString().contains("dark")).orElse(false);
        }
        return false;
    }
}