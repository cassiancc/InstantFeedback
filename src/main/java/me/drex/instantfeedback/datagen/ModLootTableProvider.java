package me.drex.instantfeedback.datagen;

import me.drex.instantfeedback.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    protected ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        this.dropSelf(ModBlocks.CARVED_PALE_PUMPKIN);
        this.dropSelf(ModBlocks.PALE_PUMPKIN);
        this.dropSelf(ModBlocks.PALE_ROSE);
        this.add(ModBlocks.PALE_BUSH, this.createSilkTouchOrShearsDispatchTable(ModBlocks.PALE_BUSH, LootItem.lootTableItem(ModBlocks.PALE_BUSH)));
        LootItemCondition.Builder hasShears = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
        LootItemCondition.Builder hasSilkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

        this.add(ModBlocks.TALL_PALE_BUSH, block ->
                this.createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                        .withPool(LootPool.lootPool()
                                .when(hasShears.or(hasSilkTouch))
                                .add(LootItem.lootTableItem(block))
                        )
        );
        this.dropSelf(ModBlocks.CERULEAN_FROGLIGHT);
        this.dropPottedContents(ModBlocks.POTTED_PALE_ROSE);
        this.dropPottedContents(ModBlocks.POTTED_CACTUS_FLOWER);
        this.dropPottedContents(ModBlocks.POTTED_ROSE_BUSH);
        this.dropPottedContents(ModBlocks.POTTED_PEONY);
        this.dropPottedContents(ModBlocks.POTTED_LILAC);
        this.dropPottedContents(ModBlocks.POTTED_SUNFLOWER);
        this.dropPottedContents(ModBlocks.POTTED_PITCHER_PLANT);
    }
}
