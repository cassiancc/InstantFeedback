package me.drex.instantfeedback.mixin.sulfur_fire;

import com.llamalad7.mixinextras.sugar.Local;
import me.drex.instantfeedback.block.ModBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    @ModifyArg(
            method = "cookTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Containers;dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"
            ),
            index = 4
    )
    private static ItemStack nauseaFood(ItemStack itemStack, @Local(argsOnly = true) BlockState state) {
        if (state.is(ModBlocks.SULFUR_CAMPFIRE)) {
            itemStack.getOrCreateTag().putBoolean("SulfurCooked", true);
        }
        return itemStack;
    }
}