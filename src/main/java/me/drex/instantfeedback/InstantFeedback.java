package me.drex.instantfeedback;

import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import me.drex.instantfeedback.block.ModBlocks;
import me.drex.instantfeedback.config.ConfigManager;
import me.drex.instantfeedback.entity.ModFrogVariants;
import me.drex.instantfeedback.entity.ModPigVariants;
import me.drex.instantfeedback.item.ModCauldronInteraction;
import me.drex.instantfeedback.item.ModItems;
import me.drex.instantfeedback.worldgen.FallenDarkOakTrunkPlacer;
import me.drex.instantfeedback.worldgen.ModVegetationPlacements;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import me.drex.instantfeedback.block.CarvedPalePumpkinBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InstantFeedback implements ModInitializer {

    public static final String MOD_ID = "instantfeedback";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ResourceKey<Registry<PigVariant>> PIG_VARIANT_REGISTRY =
            ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "pig_variant"));

    public static final SimpleParticleType CREAKING_EYES = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            new ResourceLocation(MOD_ID, "creaking_eyes"),
            FabricParticleTypes.simple()
    );
    public static final SimpleParticleType SULFUR_FLAME = Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            new ResourceLocation(MOD_ID, "sulfur_flame"),
            FabricParticleTypes.simple()
    );

    public static final TrunkPlacerType<FallenDarkOakTrunkPlacer> FALLEN_DARK_OAK_TRUNK_PLACER =
            Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, new ResourceLocation(MOD_ID, "fallen_dark_oak_trunk_placer"), new TrunkPlacerType<>(FallenDarkOakTrunkPlacer.CODEC)
    );

    @Override
    public void onInitialize() {
        ConfigManager.load();

        LOGGER.info("Initializing Instant Feedback Variants: {}, {}",
                ModPigVariants.MUDDY.location(),
                ModFrogVariants.DARK.location()
        );

        ModBlocks.initialize();
        ModItems.initialize();

        DispenserBlock.registerBehavior(ModBlocks.CARVED_PALE_PUMPKIN, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                Level level = source.getLevel();
                BlockPos blockPos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                CarvedPalePumpkinBlock carvedPalePumpkinBlock = (CarvedPalePumpkinBlock) ModBlocks.CARVED_PALE_PUMPKIN;

                if (level.isEmptyBlock(blockPos) && carvedPalePumpkinBlock.canSpawnGolem(level, blockPos)) {
                    if (!level.isClientSide) {
                        level.setBlock(blockPos, carvedPalePumpkinBlock.defaultBlockState(), 3);
                        level.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    }

                    stack.shrink(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                }

                return stack;
            }
        });

        ResourceKey<Biome> PALE_GARDEN = ResourceKey.create(Registries.BIOME, new ResourceLocation("minecraft", "pale_garden"));

        if (ConfigManager.config().theGardenAwakensRemoveMobSpawn) {
            BiomeModifications.create(new ResourceLocation(MOD_ID, "pale_garden_remove_spawn"))
                .add(ModificationPhase.REMOVALS, context -> context.getBiomeKey().equals(PALE_GARDEN), context -> {
                    context.getSpawnSettings().clearSpawns();
                });
        }
        if (ConfigManager.config().theGardenAwakensWorldGen) {
            BiomeModifications.create(new ResourceLocation(MOD_ID, "pale_garden_replace_vegetation"))
                    .add(ModificationPhase.REPLACEMENTS, context -> context.getBiomeKey().equals(PALE_GARDEN), context -> {
                        ResourceKey<net.minecraft.world.level.levelgen.placement.PlacedFeature> VANILLA_PALE_VEG =
                                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("minecraft", "pale_garden_vegetation"));

                        context.getGenerationSettings().removeFeature(VANILLA_PALE_VEG);
                        context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModVegetationPlacements.PALE_GARDEN_VEGETATION);
                    });

            BiomeModifications.addFeature(
                    context -> context.getBiomeKey().equals(PALE_GARDEN),
                    GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                    ModVegetationPlacements.PATCH_PALE_PUMPKIN
            );

            ResourceKey<net.minecraft.world.level.levelgen.placement.PlacedFeature> LEAF_LITTER_KEY =
                    ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("minecraft", "leaf_litter"));

            BiomeModifications.addFeature(
                    context -> context.getBiomeKey().equals(PALE_GARDEN),
                    GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                    LEAF_LITTER_KEY
                    // ModVegetationPlacements.PILE_PALE_LEAVES
            );

            BiomeModifications.addFeature(
                    context -> context.getBiomeKey().equals(PALE_GARDEN),
                    GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                    ModVegetationPlacements.PALE_VEGETATION
            );
        }

        ComposterBlock.COMPOSTABLES.put(ModItems.PALE_BUSH.asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.TALL_PALE_BUSH.asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModItems.PALE_ROSE.asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.PALE_PUMPKIN.asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CARVED_PALE_PUMPKIN.asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.GLOWING_VINES.asItem(), 0.5F);

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (BuiltInLootTables.SNIFFER_DIGGING.equals(id)) {
                if (ConfigManager.config().trailsTalesSnifferDigGlowingVines) {
                    tableBuilder.modifyPools(builder -> builder.add(LootItem.lootTableItem(ModItems.GLOWING_VINES)));
                }
            }
        });

        ResourceConditions.register(new ResourceLocation("instantfeedback", "config"), jsonObj -> {
            String option = jsonObj.has("option")
                    ? GsonHelper.getAsString(jsonObj, "option")
                    : GsonHelper.getAsString(jsonObj, "options");

            return ConfigManager.enabledFeatures().contains(option);
        });

        ModCauldronInteraction.bootstrap();
    }

    public class PaleBonemealGrowth {
        public static void grow(ServerLevel level, BlockPos pos, RandomSource random) {
            level.getBiome(pos).unwrapKey().ifPresent(key -> {
                if (key.location().getPath().contains("pale_garden")) {

                    for (int i = 0; i < 10; ++i) {
                        BlockPos targetPos = pos.offset(random.nextInt(5) - 2, 1, random.nextInt(5) - 2);

                        if (level.getBlockState(targetPos).isAir() &&
                                level.getBlockState(targetPos.below()).is(Blocks.GRASS_BLOCK)) {

                            double chance = random.nextDouble();

                            // Carved Pale Pumpkin 0.5% chance
                            if (chance < 0.005) {
                                BlockState carved = ModBlocks.CARVED_PALE_PUMPKIN.defaultBlockState();
                                Direction randomFacing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                                level.setBlock(targetPos, carved.setValue(BlockStateProperties.HORIZONTAL_FACING, randomFacing), 3);
                            }
                            // Pale Pumpkin 1% chance
                            else if (chance < 0.015) {
                                level.setBlock(targetPos, ModBlocks.PALE_PUMPKIN.defaultBlockState(), 3);
                            }
                            // Pale Bush 20% chance
                            else if (chance < 0.215) {
                                level.setBlock(targetPos, ModBlocks.PALE_BUSH.defaultBlockState(), 3);
                            }
                            // Pale Rose 20% chance
                            else if (chance < 0.415) {
                                level.setBlock(targetPos, ModBlocks.PALE_ROSE.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            });
        }
    }

    public static String id(String path) {
        return new ResourceLocation(MOD_ID, path).toString();
    }
}