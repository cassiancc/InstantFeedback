package me.drex.instantfeedback.datagen;

import me.drex.instantfeedback.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        suspiciousStew(exporter, ModBlocks.PALE_ROSE);
        oneToOneDye(exporter, ModBlocks.PALE_ROSE, Items.WHITE_DYE);
    }

    public void suspiciousStew(Consumer<FinishedRecipe> exporter, ItemLike flower) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.SUSPICIOUS_STEW)
                .requires(Items.BOWL)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(flower)
                .group("suspicious_stew")
                .unlockedBy(getHasName(flower), has(flower))
                .save(exporter, getItemName(Items.SUSPICIOUS_STEW) + "_from_" + getItemName(flower));
    }
    public void oneToOneDye(Consumer<FinishedRecipe> exporter, ItemLike flower, ItemLike dye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dye)
                .requires(flower)
                .group("dye")
                .unlockedBy(getHasName(flower), has(flower))
                .save(exporter, getItemName(dye) + "_from_" + getItemName(flower));
    }

    @Override
    public String getName() {
        return "InstantFeedback Recipes";
    }
}