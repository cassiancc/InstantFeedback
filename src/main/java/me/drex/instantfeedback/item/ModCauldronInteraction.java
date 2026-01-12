package me.drex.instantfeedback.item;

import me.drex.instantfeedback.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.blackgear.vanillabackport.common.registries.ModItems.*;

public class ModCauldronInteraction {
    public static List<Supplier<Item>> getAllBundleItemColors() {
        return List.of(
                                WHITE_BUNDLE,
                                ORANGE_BUNDLE,
                                MAGENTA_BUNDLE,
                                LIGHT_BLUE_BUNDLE,
                                YELLOW_BUNDLE,
                                LIME_BUNDLE,
                                PINK_BUNDLE,
                                GRAY_BUNDLE,
                                LIGHT_GRAY_BUNDLE,
                                CYAN_BUNDLE,
                                BLACK_BUNDLE,
                                BROWN_BUNDLE,
                                GREEN_BUNDLE,
                                RED_BUNDLE,
                                BLUE_BUNDLE,
                                PURPLE_BUNDLE
                );
    }


    public static void bootstrap() {
        Map<Item, CauldronInteraction> map = CauldronInteraction.WATER.map();
        if (ConfigManager.config().chaseTheSkiesUndyeBundles) {
            for (Supplier<Item> bundleItem : getAllBundleItemColors()) {
                map.put(bundleItem.get(), ModCauldronInteraction::bundleInteraction);
            }
        }
    
    }

    private static ItemInteractionResult bundleInteraction(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (!level.isClientSide()) {
            ItemStack cleanedBundle = itemStack.transmuteCopy(Items.BUNDLE, 1);
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, cleanedBundle, false));
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
            LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
        }
        return ItemInteractionResult.SUCCESS;
    }
}
