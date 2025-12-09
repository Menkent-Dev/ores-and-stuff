package net.menkent.oresandstuff.recipe;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.menkent.oresandstuff.OresNStuff;
import net.menkent.oresandstuff.blockentity.CrucibleBlockEntity;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.references.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CrucibleRecipe implements Recipe<CrucibleRecipeInput> {

    private final ItemStack output;
    private final NonNullList<Ingredient> ingredients;
    private final int cookingTime;
    private final float experience;
    private final ResourceLocation id;
    private final String group;
    
    public CrucibleRecipe(
        ResourceLocation id, 
        NonNullList<Ingredient> ingredients, 
        ItemStack output, 
        int cookingTime, 
        float experience,
        String group
    ) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.group = group;
    }

    public CrucibleRecipe(
        ResourceLocation id, 
        NonNullList<Ingredient> ingredients, 
        ItemStack output, 
        int cookingTime, 
        float experience
    ) {
        this(id, ingredients, output, cookingTime, experience, "");
    }


    @Override
    public ItemStack assemble(CrucibleRecipeInput recipeInput, Provider provider) {
        return output.copy();
    
    }

    public ItemStack getResultItem(Provider provider) {
        return output.copy();
    }


    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public boolean matches(CrucibleRecipeInput recipeInput, Level level) {
    ItemStack[] availableItems = new ItemStack[9];
    for (int i = 0; i < 9; i++) {
        availableItems[i] = recipeInput.getItem(i).copy();
    }
    
    // For each ingredient in the recipe
    for (Ingredient ingredient : ingredients) {
        boolean foundMatch = false;
        
        // Try to find a matching item in any slot
        for (int i = 0; i < 9; i++) {
            if (!availableItems[i].isEmpty() && ingredient.test(availableItems[i])) {
                // Found a match - "consume" this item
                availableItems[i].shrink(1);
                foundMatch = true;
                break;
            }
        }
        
        // If we couldn't find a match for this ingredient, recipe doesn't match
        if (!foundMatch) {
            return false;
        }
    }
    
    return true;
}

    @Override
    public RecipeSerializer<? extends Recipe<CrucibleRecipeInput>> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<CrucibleRecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipes.CRUCIBLE;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void consumeInputs(CrucibleBlockEntity crucible) {
        // For each ingredient, find and remove one matching item
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = crucible.getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    crucible.removeItem(i, 1);
                    break;
                }
            }
        }
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= ingredients.size(); // Need enough space for ingredients
    }

    public static Optional<RecipeHolder<CrucibleRecipe>> getRecipeHolderFor(CrucibleBlockEntity crucible, Level level) {
        if (level.isClientSide()) return Optional.empty();
        
        CrucibleRecipeInput container = new CrucibleRecipeInput(crucible);
        return level.getServer().getRecipeManager()
            .getRecipeFor(Type.INSTANCE, container, level);
    }
    

    public static CrucibleRecipe getRecipeFor(CrucibleBlockEntity crucible, Level level) {
        return getRecipeHolderFor(crucible, level)
            .map(RecipeHolder::value)
            .orElse(null);
    }

    public static class Type implements RecipeType<CrucibleRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "crucible";
        
        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = 
            ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "crucible");
        
        private static final MapCodec<CrucibleRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> 
            instance.group(
                // List of ingredients (1-9 items, order doesn't matter)
                Ingredient.CODEC
                    .listOf(1, 9)
                    .fieldOf("ingredients")
                    .flatXmap(list -> {
                        OresNStuff.LOGGER.info("Parsing ingredients list, size: " + list.size());
                        if (list.isEmpty() || list.size() > 9) {
                            return DataResult.error(() -> 
                                "Crucible recipe must have 1 to 9 ingredients, got " + list.size());
                        }
                        // Convert to NonNullList
                        NonNullList<Ingredient> nonNullList = NonNullList.create();
                        nonNullList.addAll(list);
                        return DataResult.success(nonNullList);
                    }, DataResult::success)
                    .forGetter(recipe -> recipe.ingredients),
                
                // Output item
                ItemStack.CODEC
                    .fieldOf("result")
                    .forGetter(recipe -> recipe.output),
                
                // Optional cooking time
                Codec.INT
                    .optionalFieldOf("cookingtime", 240)
                    .forGetter(recipe -> recipe.cookingTime),
                
                // Optional experience
                Codec.FLOAT
                    .optionalFieldOf("experience", 0.0f)
                    .forGetter(recipe -> recipe.experience)
            )
            .apply(instance, (ingredients, result, cookingTime, experience) -> 
                new CrucibleRecipe(null, ingredients, result, cookingTime, experience))

        );
        
        private static final StreamCodec<RegistryFriendlyByteBuf, CrucibleRecipe> STREAM_CODEC = 
            StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
        
        @Override
        public MapCodec<CrucibleRecipe> codec() {
            return MAP_CODEC;
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrucibleRecipe> streamCodec() {
            return STREAM_CODEC;
        }
        
        private static CrucibleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.create();
            
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }
            
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int cookingTime = buffer.readVarInt();
            float experience = buffer.readFloat();
            
            return new CrucibleRecipe(id, ingredients, result, cookingTime, experience);
        }
        
        private static void toNetwork(RegistryFriendlyByteBuf buffer, CrucibleRecipe recipe) {
            buffer.writeResourceLocation(recipe.id);
            // Write ingredient count
            buffer.writeVarInt(recipe.ingredients.size());
            
            // Write ingredients
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
            
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeVarInt(recipe.cookingTime);
            buffer.writeFloat(recipe.experience);
        }
    }
}
