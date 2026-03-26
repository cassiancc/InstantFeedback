package me.drex.instantfeedback.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

public class TallDryVegetationBlock extends DoublePlantBlock {
    public TallDryVegetationBlock(Properties properties) {super(properties);}

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT)
                || state.is(BlockTags.SAND)
                || state.is(Blocks.TERRACOTTA)
                || state.is(BlockTags.TERRACOTTA)
                || state.is(com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_MOSS_BLOCK.get())
                || super.mayPlaceOn(state, level, pos);
    }
}