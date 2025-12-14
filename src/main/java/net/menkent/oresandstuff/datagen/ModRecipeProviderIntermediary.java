package net.menkent.oresandstuff.datagen;

import net.menkent.oresandstuff.datagen.recipe.CrucibleRecipeBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ModRecipeProviderIntermediary {
    protected final HolderLookup.Provider registries;
    private final HolderGetter<Item> items;

    public ModRecipeProviderIntermediary(Provider provider, RecipeOutput recipeOutput) {
        this.registries = provider;
        this.items = provider.lookupOrThrow(Registries.ITEM);

    }

    public CrucibleRecipeBuilder crucible(ItemLike itemLike) {
        return CrucibleRecipeBuilder.crucible(this.items, itemLike);
    }

    public CrucibleRecipeBuilder crucible(ItemStack itemStack) {
        return CrucibleRecipeBuilder.crucible(this.items, itemStack);
    }

}
