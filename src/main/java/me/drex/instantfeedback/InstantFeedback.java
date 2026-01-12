package me.drex.instantfeedback;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
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
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicates;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.intellij.lang.annotations.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.blackgear.vanillabackport.common.registries.ModBiomes.PALE_GARDEN;
import static me.drex.instantfeedback.worldgen.ModVegetationPlacements.PALE_GARDEN_VEGETATION;

public class InstantFeedback implements ModInitializer {

    public static final String MOD_ID = "instantfeedback";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final SimpleParticleType CREAKING_EYES = FabricParticleTypes.simple();
    public static final ParticleType<ColorParticleOption> TINTED_NEEDLES = FabricParticleTypes.complex(ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final TrunkPlacerType<FallenDarkOakTrunkPlacer> FALLEN_DARK_OAK_TRUNK_PLACER = Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "fallen_dark_oak_trunk_placer"), new TrunkPlacerType<>(FallenDarkOakTrunkPlacer.CODEC));


    @Override
    public void onInitialize() {
        ConfigManager.load();
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "creaking_eyes"), CREAKING_EYES);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "tinted_needles"), TINTED_NEEDLES);
        ModBlocks.initialize();
        ModFrogVariants.inititalize();
        ModItems.initialize();
        if (ConfigManager.config().theGardenAwakensRemoveMobSpawn) {
            BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "pale_garden_remove_spawn"))
                .add(ModificationPhase.REMOVALS, context -> context.getBiomeKey() == PALE_GARDEN, context -> {
                    context.getSpawnSettings().clearSpawns();
                });
        }
        if (ConfigManager.config().theGardenAwakensWorldGen) {
            BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "pale_garden_replace_vegetation"))
                .add(ModificationPhase.REPLACEMENTS, context -> context.getBiomeKey() == PALE_GARDEN, context -> {
                    context.getGenerationSettings().removeFeature(PALE_GARDEN_VEGETATION);
                    context.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PALE_GARDEN_VEGETATION);
                });
            BiomeModifications.addFeature(
                context -> context.getBiomeKey() == PALE_GARDEN,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModVegetationPlacements.PATCH_PALE_PUMPKIN
            );
            BiomeModifications.addFeature(
                context -> context.getBiomeKey() == PALE_GARDEN,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModVegetationPlacements.PILE_PALE_LEAVES
            );
            BiomeModifications.addFeature(
                context -> context.getBiomeKey() == PALE_GARDEN,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModVegetationPlacements.PALE_VEGETATION
            );
        }

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            var magmaCube = EntityType.MAGMA_CUBE.getDefaultLootTable();
            if (magmaCube.equals(key) && source.isBuiltin()) {
                tableBuilder.modifyPools(builder -> {
                    builder.add(
                            LootItem.lootTableItem(ModItems.CERULEAN_FROGLIGHT)
                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                    .when(DamageSourceCondition.hasDamageSource(
                                            DamageSourcePredicate.Builder.damageType()
                                                    .source(
                                                            EntityPredicate.Builder.entity()
                                                                    .of(EntityType.FROG)
                                                                    .subPredicate(EntitySubPredicates.frogVariant(BuiltInRegistries.FROG_VARIANT.getHolder(ModFrogVariants.DARK).get()))
                                                    )
                                    ))
                    );
                });
            }
        });

        ModCauldronInteraction.bootstrap();

        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer -> {
            ModPigVariants.bootstrap(minecraftServer.registryAccess());
        }));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}