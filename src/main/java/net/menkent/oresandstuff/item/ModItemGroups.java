package net.menkent.oresandstuff.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.menkent.oresandstuff.OresNStuff;
import net.menkent.oresandstuff.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final CreativeModeTab ORES_N_STUFF_ITEM_GROUP = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB, 
        ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "ores_n_stuff_items"), 
        FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.CRUCIBLE_BLOCK))
            .title(Component.translatable("itemgroup.ores_and_stuff.ores_n_stuff_items"))
            .displayItems((displayContext, entries) -> {
                entries.accept(ModBlocks.CRUCIBLE_BLOCK);
            })
            .build()
        );

    public static void register() {
        OresNStuff.LOGGER.info("Registering Item Groups");
    }
}
