package net.menkent.oresandstuff.datagen.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.menkent.oresandstuff.recipe.CrucibleRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class CrucibleRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final ItemStack result;
    private final List<String> ingredients = new ArrayList<>();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    private int cookingTime = 240;
    private float experience = 0.0f;
    private boolean showNotification = true;

    private CrucibleRecipeBuilder(HolderGetter<Item> holderGetter, ItemStack itemStack) {
        this.items = holderGetter;
        this.result = itemStack;
    }

    public static CrucibleRecipeBuilder crucible(HolderGetter<Item> holderGetter, ItemStack itemStack) {
        return crucible(holderGetter, itemStack, 1);
    }

    public static CrucibleRecipeBuilder crucible(HolderGetter<Item> holderGetter, ItemStack itemStack, int i) {
        return new CrucibleRecipeBuilder(holderGetter, itemStack.copyWithCount(i));
    }

    public static CrucibleRecipeBuilder crucible(HolderGetter<Item> holderGetter, ItemLike itemLike) {
        return crucible(holderGetter, itemLike, 1);
    }

    public static CrucibleRecipeBuilder crucible(HolderGetter<Item> holderGetter, ItemLike itemLike, int i) {
        return new CrucibleRecipeBuilder(holderGetter, itemLike.asItem().getDefaultInstance().copyWithCount(i));
    }


    public CrucibleRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
    }

    public CrucibleRecipeBuilder ingredient(TagKey<Item> tag) {
        return this.ingredient("#" + tag.location().toString());
    }

    public CrucibleRecipeBuilder ingredient(ItemLike item, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredient(item);
        }
        return this;
    }

    public CrucibleRecipeBuilder ingredient(TagKey<Item> tag, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredient(tag);
        }
        return this;
    }

    public CrucibleRecipeBuilder ingredient(String itemId) {
        if (ingredients.size() >= 9) {
            throw new IllegalStateException("Crucible recipes can have at most 9 ingredients!");
        }
        this.ingredients.add(itemId);
        return this;
    }

    public CrucibleRecipeBuilder cookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public CrucibleRecipeBuilder experience(float experience) {
        this.experience = experience;
        return this;
    }

    @Override
    public CrucibleRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterion) {
        this.criteria.put(criterionName, criterion);
        return this;
    }

    @Override
    public CrucibleRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public CrucibleRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> recipeKey) {
        this.ensureValid(recipeKey);
        ResourceLocation id = recipeKey.location();
        Map<String, Criterion<?>> criteriaIntermediary = this.criteria;
        Advancement.Builder advancementBuilder = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
            .rewards(AdvancementRewards.Builder.recipe(recipeKey))
            .requirements(AdvancementRequirements.Strategy.OR);

            NonNullList<Ingredient> ingredientList = NonNullList.create();
            for (String ingredientString : ingredients) {
                if (ingredientString.startsWith("#")) {
                    ResourceLocation tagLocation = ResourceLocation.parse(ingredientString.substring(1));
                    TagKey<Item> tag = TagKey.create(net.minecraft.core.registries.Registries.ITEM, tagLocation);
                    ingredientList.add(Ingredient.of(items.getOrThrow(tag)));
                } else {
                    ResourceLocation itemLocation = ResourceLocation.parse(ingredientString);
                    ingredientList.add(Ingredient.of(items.getOrThrow(ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, itemLocation)).value()));
                }
            }

        CrucibleRecipe recipe = new CrucibleRecipe(
            id,
            ingredientList,
            result,
            cookingTime,
            experience
        );

        criteriaIntermediary.forEach(advancementBuilder::addCriterion);
        output.accept(recipeKey, recipe, advancementBuilder.build(id.withPrefix("recipe/" + "crucible" + "/")));
    }

    private void ensureValid(ResourceKey<Recipe<?>> resourceKey) {
        if (this.criteria.isEmpty()) {
           throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(resourceKey.location()));
        }
    }
}