package net.menkent.oresandstuff.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.menkent.oresandstuff.block.ModBlocks;
import net.menkent.oresandstuff.item.ModItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Ores N' Stuff Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            ModRecipeProviderIntermediary recipeProviderIntermediary = new ModRecipeProviderIntermediary(registryLookup, exporter);

            @Override
            public void buildRecipes() {
                createRecipeProvider(registryLookup, exporter).shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STEEL_BLOCK)
                    .requires(ModItems.STEEL_INGOT, 9)
                    .unlockedBy("has_steel_ingot", createRecipeProvider(registryLookup,exporter).has(ModItems.STEEL_INGOT))
                    .save(exporter);

                createRecipeProvider(registryLookup, exporter).shapeless(RecipeCategory.MISC, ModItems.STEEL_INGOT)
                    .requires(ModBlocks.STEEL_BLOCK)
                    .unlockedBy("has_steel_ingot", createRecipeProvider(registryLookup,exporter).has(ModItems.STEEL_INGOT))
                    .save(exporter, ResourceKey.create(Registries.RECIPE, ResourceLocation.parse("ores_and_stuff:steel_ingot_from_block")));

                createRecipeProvider(registryLookup, exporter).shaped(RecipeCategory.DECORATIONS, ModBlocks.CRUCIBLE)
                    .define('#', Blocks.STONE).define('i', Items.IRON_INGOT)
                    .define('u', Blocks.CAULDRON)
                    .define('o', Blocks.GLASS_PANE)
                    .pattern("ioi")
                    .pattern("#u#")
                    .pattern("#i#")
                    .unlockedBy("has_cauldron", createRecipeProvider(registryLookup,exporter).has(Blocks.CAULDRON))
                    .save(exporter);

                recipeProviderIntermediary.crucible(ModItems.STEEL_INGOT)
                    .ingredient("minecraft:coal")
                    .ingredient("minecraft:iron_ingot")
                    .cookingTime(400)
                    .experience(2.5f)
                    .unlockedBy("has_iron_and_coal", createRecipeProvider(registryLookup, exporter).has(Items.IRON_INGOT))
                    .save(exporter);
                
                recipeProviderIntermediary.crucibleSmelting(Items.GOLD_INGOT, Items.GOLD_ORE, 1.0F, Items.GOLD_ORE);
                recipeProviderIntermediary.crucibleSmelting(Items.IRON_INGOT, Items.IRON_ORE, 0.7F, Items.IRON_ORE);
                recipeProviderIntermediary.crucibleSmelting(Items.COPPER_INGOT, Items.COPPER_ORE, 0.7F, Items.COPPER_ORE);
            }
            
        };
    }

}
