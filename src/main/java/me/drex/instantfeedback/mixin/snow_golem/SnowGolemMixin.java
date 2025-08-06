package me.drex.instantfeedback.mixin.snow_golem;

import me.drex.instantfeedback.duck.snow_golem.ISnowGolem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem implements ISnowGolem {

    @Unique
    private static final EntityDataAccessor<Byte> instantfeedback$DATA_PALE_PUMPKIN_ID = SynchedEntityData.defineId(SnowGolem.class, EntityDataSerializers.BYTE);
    @Unique
    private static final byte instantfeedback$PALE_PUMPKIN_FLAG = 32;

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "defineSynchedData",
        at = @At("TAIL")
    )
    public void instantfeedback$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(instantfeedback$DATA_PALE_PUMPKIN_ID, (byte)0);
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
        if (tag.contains("Pumpkin")) {
            this.instantfeedback$setPalePumpkin(tag.getBoolean("PalePumpkin"));
        }
    }

    @ModifyVariable(
            method = "performRangedAttack",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            ), index = 1
    )
    public LivingEntity instantfeedback$specialEffects(LivingEntity value) {
        if (instantfeedback$hasPalePumpkin()) {
            value.setRemainingFireTicks(100);
        }
        return value;
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
}
