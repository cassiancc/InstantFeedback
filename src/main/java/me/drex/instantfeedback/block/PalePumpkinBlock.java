package me.drex.instantfeedback.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class PalePumpkinBlock extends Block {
    public static final MapCodec<PalePumpkinBlock> CODEC = simpleCodec(PalePumpkinBlock::new);

    @Override
    public MapCodec<PalePumpkinBlock> codec() {
        return CODEC;
    }

    public PalePumpkinBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!itemStack.is(ConventionalItemTags.SHEAR_TOOLS)) {
            return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
        } else if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Direction direction = blockHitResult.getDirection();
            Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
            level.playSound(null, blockPos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(blockPos, ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState().setValue(CarvedPalePumpkinBlock.FACING, direction2), 11);
            ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + (double)0.5F + (double)direction2.getStepX() * 0.65, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + (double)0.5F + (double)direction2.getStepZ() * 0.65, new ItemStack(Items.PUMPKIN_SEEDS, 4));
            itemEntity.setDeltaMovement(0.05 * (double)direction2.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction2.getStepZ() + level.random.nextDouble() * 0.02);
            level.addFreshEntity(itemEntity);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(interactionHand));
            level.gameEvent(player, GameEvent.SHEAR, blockPos);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            return InteractionResult.SUCCESS;
        }
    }
}
