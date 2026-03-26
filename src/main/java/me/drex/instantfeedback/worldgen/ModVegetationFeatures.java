package me.drex.instantfeedback.worldgen;

import com.google.common.collect.ImmutableList;
import me.drex.instantfeedback.InstantFeedback;
import me.drex.instantfeedback.block.CarvedPalePumpkinBlock;
import me.drex.instantfeedback.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
// import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PaleMossDecorator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.OptionalInt;

public class ModVegetationFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PALE_PUMPKIN = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(InstantFeedback.MOD_ID, "patch_pale_pumpkin"));
    // public static final ResourceKey<ConfiguredFeature<?, ?>> PILE_PALE_LEAVES = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(InstantFeedback.MOD_ID, "pile_pale_leaves"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_VEGETATION = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(InstantFeedback.MOD_ID, "pale_vegetation"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_PALE_OAK_CREAKING = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(InstantFeedback.MOD_ID, "fallen_pale_oak_creaking"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(InstantFeedback.MOD_ID, "pale_garden_vegetation"));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        HolderGetter<PlacedFeature> placedFeatures = bootstrapContext.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> paleOakChecked = placedFeatures.getOrThrow(com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements.PALE_OAK_CHECKED);
        Holder<PlacedFeature> paleOakCreakingChecked = placedFeatures.getOrThrow(com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements.PALE_OAK_CREAKING_CHECKED);
        Holder<PlacedFeature> fallenPaleOakCreakingChecked = placedFeatures.getOrThrow(ModVegetationPlacements.FALLEN_PALE_OAK_CREAKING);

        FeatureUtils.register(
            bootstrapContext,
            ModVegetationFeatures.PATCH_PALE_PUMPKIN,
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.PALE_PUMPKIN.defaultBlockState(), 20)
                        .add(ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState().setValue(CarvedPalePumpkinBlock.FACING, Direction.NORTH), 1)
                        .add(ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState().setValue(CarvedPalePumpkinBlock.FACING, Direction.EAST), 1)
                        .add(ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState().setValue(CarvedPalePumpkinBlock.FACING, Direction.SOUTH), 1)
                        .add(ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState().setValue(CarvedPalePumpkinBlock.FACING, Direction.WEST), 1)
                )), List.of(Blocks.GRASS_BLOCK),
                24
            )
        );

        /* FeatureUtils.register(
            bootstrapContext, PILE_PALE_LEAVES, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true))),
                List.of(Blocks.GRASS_BLOCK, com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_OAK_LEAVES.get())
            )
        ); */

        FeatureUtils.register(
            bootstrapContext,
            PALE_VEGETATION,
            Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(164, 16, 8, PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.PALE_ROSE.defaultBlockState(), 1)
                        .add(ModBlocks.PALE_BUSH.defaultBlockState(), 5)
                        .add(ModBlocks.TALL_PALE_BUSH.defaultBlockState(), 5)
                        .add(com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState(), 10)
                        .build()
                )),
                BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), List.of(Blocks.GRASS_BLOCK)))
            )
            )
        );

        FeatureUtils.register(
            bootstrapContext,
            FALLEN_PALE_OAK_CREAKING,
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_OAK_LOG.get()),
                new FallenDarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(com.blackgear.vanillabackport.common.registries.ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
                .decorators(ImmutableList.of(new PaleMossDecorator(0.25F, 0.8F, 0.8F), new CreakingHeartDecorator(1.0F)))
                .ignoreVines()
                .build()
        );

        FeatureUtils.register(
            bootstrapContext,
            PALE_GARDEN_VEGETATION,
            Feature.RANDOM_SELECTOR,
            new RandomFeatureConfiguration(List.of(
                    new WeightedPlacedFeature(paleOakCreakingChecked, 0.2F),
                    new WeightedPlacedFeature(paleOakChecked, 0.8F),
                    new WeightedPlacedFeature(fallenPaleOakCreakingChecked, 0.2F) // Lowered from 0.8F to 0.2F, was too common
            ), paleOakChecked) // also lowered from 0.8 to 0.2 in configured_feature\pale_garden_vegetation.json
        );

    }

}
