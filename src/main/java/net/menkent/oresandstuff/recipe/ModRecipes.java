package net.menkent.oresandstuff.recipe;

import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public class ModRecipes {
    public static final RecipeBookCategory CRUCIBLE = register("crucible");

    private static RecipeBookCategory register(String string) {
      return (RecipeBookCategory)Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, string, new RecipeBookCategory());
    }

    public static void register() {
      Registry.register(BuiltInRegistries.RECIPE_TYPE, 
            ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, CrucibleRecipe.Type.ID),
            CrucibleRecipe.Type.INSTANCE
          );
        
      Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
          CrucibleRecipe.Serializer.ID,
          CrucibleRecipe.Serializer.INSTANCE
      );
          
        OresNStuff.LOGGER.info("Registering Recipes");
    }
}
