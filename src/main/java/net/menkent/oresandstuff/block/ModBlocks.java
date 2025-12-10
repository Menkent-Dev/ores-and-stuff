package net.menkent.oresandstuff.block;

import org.spongepowered.include.com.google.common.base.Function;

import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    // why so many properties :sob: (wait, this ain't discord)

    public static final Block CRUCIBLE_BLOCK = register(
        "crucible_block", 
        CrucibleBlock::new, 
        BlockBehaviour.Properties.of(),
        true
    );

    public static final Block STEEL_BLOCK = register(
		"steel_block",
		Block::new,
		BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .strength(15.0f, 25.0f)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops(),
		true
);

    public static Block register( 
        String name, 
        Function<BlockBehaviour.Properties, Block> blockFactory, 
        BlockBehaviour.Properties settings, 
        boolean shouldRegisterItem
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, name));
        Block block = blockFactory.apply(settings.setId(blockKey));
        
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, name));

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }


    public static void register() {
        OresNStuff.LOGGER.info("Registering Blocks");
    }
}
