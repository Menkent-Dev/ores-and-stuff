package net.menkent.oresandstuff.block;

import org.spongepowered.include.com.google.common.base.Function;

import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

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

    public static Block registerItemBlock(
        String name,
        MapColor mapColor,
        float hardness,
        float blastResistance,
        SoundType soundType,
        boolean shouldRegisterItem
    ) {

        return register(
            name + "_block",
            Block::new,
            BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .strength(hardness, blastResistance)
                .sound(soundType)
                .requiresCorrectToolForDrops(),
            shouldRegisterItem
        );
    }

    public static Block registerOreBlock(
        String name,
        Function<BlockBehaviour.Properties, Block> blockFactory,
        boolean shouldRegisterItem
    ) {
        return register(
            name + "_ore",
            blockFactory, 
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3.0F), 
                
            shouldRegisterItem
        );
    }

    public static Block registerDeepslateOreBlock(
        String name,
        Function<BlockBehaviour.Properties, Block> blockFactory,
        boolean shouldRegisterItem
    ) {
        return register(
            "deepslate_" + name + "_ore",
            blockFactory, 
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .sound(SoundType.DEEPSLATE)
                .requiresCorrectToolForDrops()
                .strength(4.5F, 3.0F), 
                
            shouldRegisterItem
        );
    }

    //
    // BLOCKS
    //

    public static final Block BLANK = register(
        "blank_error", 
        Block::new, 
        BlockBehaviour.Properties.of(), 
        true
    );

    public static final Block CRUCIBLE = register(
        "crucible", 
        CrucibleBlock::new, 
        BlockBehaviour.Properties.of()
            .lightLevel(state -> state.getValue(CrucibleBlock.LIT) ? 13 : 0),
        true
    );

    public static final Block STEEL_BLOCK = registerItemBlock(
        "steel", 
        MapColor.COLOR_GRAY, 
        15.0f, 
        25.0f, 
        SoundType.METAL, 
        true
    );

    public static final Block TITANIUM_BLOCK = registerItemBlock(
		"titanium",
		MapColor.TERRACOTTA_LIGHT_BLUE,
        22.5f,
        32.5f,
		SoundType.COPPER,
		true
    );


    public static final Block TITANIUM_ORE = registerOreBlock(
        "titanium",
        (properties) -> {
            return new DropExperienceBlock(ConstantInt.of(0), properties);
        },
        true
    );

    public static final Block DEEPSLATE_TITANIUM_ORE = registerDeepslateOreBlock(
        "titanium",
        (properties) -> {
            return new DropExperienceBlock(ConstantInt.of(0), properties);
        },
        true
    );

    public static void register() {
        OresNStuff.LOGGER.info("Registering Blocks");
    }
}
