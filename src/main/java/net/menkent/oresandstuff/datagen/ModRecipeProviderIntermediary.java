package net.menkent.oresandstuff.datagen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.menkent.oresandstuff.datagen.recipe.CrucibleRecipeBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.Slots;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemPredicate.Builder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ModRecipeProviderIntermediary {
    protected final HolderLookup.Provider registries;
    private final HolderGetter<Item> items;
    protected final RecipeOutput output;

    public ModRecipeProviderIntermediary(Provider provider, RecipeOutput recipeOutput) {
        this.registries = provider;
        this.items = provider.lookupOrThrow(Registries.ITEM);
        this.output = recipeOutput;

    }

    public CrucibleRecipeBuilder crucible(ItemLike itemLike) {
        return CrucibleRecipeBuilder.crucible(this.items, itemLike);
    }

    public CrucibleRecipeBuilder crucible(ItemStack itemStack) {
        return CrucibleRecipeBuilder.crucible(this.items, itemStack);
    }

    public void crucibleSmelting(ItemStack itemStack, ItemLike ingredient, ItemLike unlockedBy) {
        crucibleSmelting(itemStack.getItem(), ingredient, unlockedBy);
    }

    public void crucibleSmelting(ItemLike itemLike, ItemLike ingredient, ItemLike unlockedBy) {
        CrucibleRecipeBuilder.crucible(this.items, itemLike).ingredient(ingredient).unlockedBy(RecipeProvider.getHasName(unlockedBy), this.has(unlockedBy)).save(this.output, getItemName(itemLike) + "_from_crucible_smelting_" + getItemName(ingredient));
    }

    public Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike) {
        return inventoryTrigger(Builder.item().of(this.items, new ItemLike[]{itemLike}));
    }

    // the following functions exist on RecipeProvider

    public static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate.Builder... builders) {
        return inventoryTrigger((ItemPredicate[])Arrays.stream(builders).map(ItemPredicate.Builder::build).toArray((i) -> {
            return new ItemPredicate[i];
        }));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... itemPredicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), Slots.ANY, List.of(itemPredicates)));
    }

    public static String getItemName(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }

}
