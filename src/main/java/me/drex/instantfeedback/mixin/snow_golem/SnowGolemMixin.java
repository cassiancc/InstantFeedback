package me.drex.instantfeedback.mixin.snow_golem;

import me.drex.instantfeedback.duck.ISnowGolem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem implements ISnowGolem {

    @Unique
    private static final EntityDataAccessor<Byte> instantfeedback$DATA_PALE_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);
    @Unique
    private static final byte instantfeedback$PALE_PUMPKIN_FLAG = 32;

    @org.spongepowered.asm.mixin.Shadow public abstract void setPumpkin(boolean hasPumpkin);

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "defineSynchedData",
        at = @At("TAIL")
    )
    protected void instantfeedback$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(instantfeedback$DATA_PALE_PUMPKIN_ID, (byte)0);
    }

    @Inject(
        method = "addAdditionalSaveData",
        at = @At("TAIL")
    )
    public void instantfeedback$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("PalePumpkin", this.instantfeedback$hasPalePumpkin());
    }

    @Inject(
        method = "readAdditionalSaveData",
        at = @At("TAIL")
    )
    public void instantfeedback$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.instantfeedback$setPalePumpkin(tag.getBoolean("PalePumpkin"));
    }

    @Inject(
            method = "performRangedAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void instantfeedback$applyFireEffect(net.minecraft.world.entity.LivingEntity target, float pullProgress, CallbackInfo ci, Snowball snowball) {
        if (this.instantfeedback$hasPalePumpkin()) {
            snowball.setSecondsOnFire(5);
        }
    }

    @Override
    public void instantfeedback$setPalePumpkin(boolean palePumpkin) {
        byte b = this.entityData.get(instantfeedback$DATA_PALE_PUMPKIN_ID);
        if (palePumpkin) {
            this.entityData.set(instantfeedback$DATA_PALE_PUMPKIN_ID, (byte)(b | instantfeedback$PALE_PUMPKIN_FLAG));
        } else {
            this.entityData.set(instantfeedback$DATA_PALE_PUMPKIN_ID, (byte)(b & ~instantfeedback$PALE_PUMPKIN_FLAG));
        }
    }

    @Override
    public boolean instantfeedback$hasPalePumpkin() {
        return (this.entityData.get(instantfeedback$DATA_PALE_PUMPKIN_ID) & instantfeedback$PALE_PUMPKIN_FLAG) != 0;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void instantfeedback$shearPalePumpkin(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(Items.SHEARS) && this.instantfeedback$hasPalePumpkin()) {

            if (!this.level().isClientSide) {
                this.setPumpkin(false);
                this.instantfeedback$setPalePumpkin(false);

                itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SNOW_GOLEM_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
                this.gameEvent(GameEvent.SHEAR, player);

                ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(me.drex.instantfeedback.block.ModBlocks.CARVED_PALE_PUMPKIN));
                this.level().addFreshEntity(itemEntity);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        }
    }

}
