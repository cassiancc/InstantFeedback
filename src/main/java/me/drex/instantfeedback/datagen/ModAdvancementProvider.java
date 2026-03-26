package me.drex.instantfeedback.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.common.registries.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;

    protected ModAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output);
        this.registryLookupFuture = registryLookup;
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement vanillaParent = Advancement.Builder.advancement()
                .build(new ResourceLocation("minecraft", "end/kill_dragon"));

        Advancement.Builder.advancement()
                .parent(vanillaParent)
                .display(
                        ModItems.WHITE_HARNESS.get(),
                        Component.translatable("advancement.instantfeedback.end.ride_happy_ghast.title"),
                        Component.translatable("advancement.instantfeedback.end.ride_happy_ghast.description"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("ride_happy_ghast",
                        new StartRidingTrigger.TriggerInstance(
                                EntityPredicate.wrap(
                                        EntityPredicate.Builder.entity()
                                                .vehicle(EntityPredicate.Builder.entity()
                                                        .of(ModEntities.HAPPY_GHAST.get())
                                                        .located(LocationPredicate.Builder.location()
                                                                .setDimension(Level.END)
                                                                .build())
                                                        .build())
                                                .build()
                                )
                        )
                )
                .requirements(RequirementsStrategy.AND)
                .save(consumer, "instantfeedback:end/ride_happy_ghast");
    } // This is stupid
}
