package me.drex.instantfeedback.datagen;

import me.drex.instantfeedback.InstantFeedback;
import me.drex.instantfeedback.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE)
            .add(ModBlocks.PALE_PUMPKIN)
            .add(ModBlocks.CARVED_PALE_PUMPKIN)
            .add(ModBlocks.PALE_ROSE);

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
            .add(ModBlocks.PALE_PUMPKIN)
            .add(ModBlocks.CARVED_PALE_PUMPKIN);

        this.getOrCreateTagBuilder(TagKey.create(Registries.BLOCK, new ResourceLocation("fabric", "mineable/with_shears")))
                .add(ModBlocks.PALE_BUSH, ModBlocks.TALL_PALE_BUSH);

        this.getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
            .add(ModBlocks.PALE_PUMPKIN)
            .add(ModBlocks.CARVED_PALE_PUMPKIN)
            .add(ModBlocks.PALE_BUSH, ModBlocks.TALL_PALE_BUSH);

        this.getOrCreateTagBuilder(BlockTags.FLOWERS)
            .add(ModBlocks.PALE_ROSE);

        this.getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .add(ModBlocks.PALE_ROSE);

        this.getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
            .add(ModBlocks.POTTED_PALE_ROSE)
            .add(ModBlocks.POTTED_PALE_BUSH)
            .add(ModBlocks.POTTED_TALL_PALE_BUSH)
            .add(ModBlocks.POTTED_CACTUS_FLOWER)
            .add(ModBlocks.POTTED_ROSE_BUSH)
            .add(ModBlocks.POTTED_PEONY)
            .add(ModBlocks.POTTED_LILAC)
            .add(ModBlocks.POTTED_SUNFLOWER)
            .add(ModBlocks.POTTED_PITCHER_PLANT);

        this.getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
                .add(ModBlocks.PALE_BUSH, ModBlocks.TALL_PALE_BUSH);

        /* this.getOrCreateTagBuilder(ModBlockTags.LEAVES_NEEDLES)
            .add(Blocks.SPRUCE_LEAVES); */

        TagAppender<Block> builder = getOrCreateTagBuilder(BlockTags.REPLACEABLE);
        wrapperLookup.lookupOrThrow(Registries.BLOCK)
            .filterElements(block -> block.defaultBlockState().canBeReplaced())
            .listElementIds()
            .filter(key -> key.location().getNamespace().equals(InstantFeedback.MOD_ID))
            .forEach(builder::add);
    }
}
