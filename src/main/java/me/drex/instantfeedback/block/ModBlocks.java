package me.drex.instantfeedback.block;

import com.google.common.collect.ImmutableSet;
import me.drex.instantfeedback.InstantFeedback;
import me.drex.instantfeedback.mixin.sulfur_fire.BlockEntityTypeAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class ModBlocks {

    public static final Block SULFUR_TORCH = register(
            "sulfur_torch",
            properties -> new TorchBlock(properties, InstantFeedback.SULFUR_FLAME),
            BlockBehaviour.Properties.copy(Blocks.SOUL_TORCH)
    );

    public static final Block SULFUR_WALL_TORCH = register(
            "sulfur_wall_torch",
            properties -> new WallTorchBlock(properties, InstantFeedback.SULFUR_FLAME),
            BlockBehaviour.Properties.copy(Blocks.SOUL_WALL_TORCH)
                    .dropsLike(SULFUR_TORCH)
    );

    public static final Block SULFUR_LANTERN = register(
            "sulfur_lantern",
            LanternBlock::new,
            BlockBehaviour.Properties.copy(Blocks.SOUL_LANTERN)
    );

    public static final Block SULFUR_CAMPFIRE = register(
            "sulfur_campfire",
            properties -> new CampfireBlock(false, 2, properties),
            BlockBehaviour.Properties.copy(Blocks.SOUL_CAMPFIRE)
    );

    public static final Block SULFUR_FIRE = register(
        "sulfur_fire",
        SulfurFireBlock::new,
        BlockBehaviour.Properties.copy(Blocks.SOUL_FIRE)
                .mapColor(MapColor.COLOR_MAGENTA)
    );

    public static final Block PALE_PUMPKIN = register(
        "pale_pumpkin",
        PalePumpkinBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.DIDGERIDOO)
            .strength(1.0F)
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block CARVED_PALE_PUMPKIN = register(
        "carved_pale_pumpkin",
        CarvedPalePumpkinBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(1.0F)
            .sound(SoundType.WOOD)
            .isValidSpawn(Blocks::always)
            .pushReaction(PushReaction.DESTROY)
    );

    public static final Block PALE_ROSE = register(
        "pale_rose",
        properties -> new FlowerBlock(MobEffects.GLOWING, 15, properties),
        BlockBehaviour.Properties.of()
            .mapColor(DyeColor.WHITE)
            .noCollission()
            .noOcclusion()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .lightLevel(state -> 3)
            .pushReaction(PushReaction.DESTROY));

    public static final Block PALE_BUSH = register(
            "pale_bush",
            DryVegetationBlock::new,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .replaceable()
                .noCollission()
                .noOcclusion()
                .instabreak()
                .sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
    );

    public static final Block TALL_PALE_BUSH = register(
            "tall_pale_bush",
            TallDryVegetationBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .replaceable()
                    .noCollission()
                    .noOcclusion()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final Block POTTED_PALE_ROSE = register("potted_pale_rose",
        properties -> new FlowerPotBlock(PALE_ROSE, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().lightLevel(state -> 3).pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_PALE_BUSH = register("potted_pale_bush",
            properties -> new FlowerPotBlock(PALE_BUSH, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
    );

    public static final Block POTTED_TALL_PALE_BUSH = register("potted_tall_pale_bush",
            properties -> new FlowerPotBlock(TALL_PALE_BUSH, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
    );

    public static final Block POTTED_CACTUS_FLOWER = register("potted_cactus_flower",
        properties -> new FlowerPotBlock(com.blackgear.vanillabackport.common.registries.ModBlocks.CACTUS_FLOWER.get(), properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_ROSE_BUSH = register("potted_rose_bush",
        properties -> new FlowerPotBlock(Blocks.ROSE_BUSH, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_PEONY = register("potted_peony",
        properties -> new FlowerPotBlock(Blocks.PEONY, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_LILAC = register("potted_lilac",
        properties -> new FlowerPotBlock(Blocks.LILAC, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_SUNFLOWER = register("potted_sunflower",
        properties -> new FlowerPotBlock(Blocks.SUNFLOWER, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block POTTED_PITCHER_PLANT = register("potted_pitcher_plant",
        properties -> new FlowerPotBlock(Blocks.PITCHER_PLANT, properties), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

    public static final Block CERULEAN_FROGLIGHT = register(
            "cerulean_froglight",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(0.3f)
                    .sound(SoundType.FROGLIGHT)
                    .lightLevel(blockStatex -> 15)
    );

    public static void initialize() {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) BlockEntityType.CAMPFIRE;

        accessor.setValidBlocks(
                ImmutableSet.<Block>builder()
                        .addAll(accessor.getValidBlocks())
                        .add(SULFUR_CAMPFIRE)
                        .build()
        );
    }

    public static Block register(Block block, String path) {
        ResourceLocation id = new ResourceLocation(InstantFeedback.MOD_ID, path);
        BlockItem blockItem = new BlockItem(block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, id, blockItem);
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static Block register(ResourceKey<Block> resourceKey, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        Block block = function.apply(properties);
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    public static Block register(ResourceKey<Block> resourceKey, BlockBehaviour.Properties properties) {
        return register(resourceKey, Block::new, properties);
    }

    private static ResourceKey<Block> blockId(String path) {
        return ResourceKey.create(Registries.BLOCK, new ResourceLocation(InstantFeedback.MOD_ID, path));
    }

    private static Block register(String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        return register(blockId(string), function, properties);
    }

    private static Block register(String string, BlockBehaviour.Properties properties) {
        return register(string, Block::new, properties);
    }

}
