package net.menkent.oresandstuff.recipe;

import net.menkent.oresandstuff.blockentity.CrucibleBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CrucibleRecipeInput implements RecipeInput {
    private final CrucibleBlockEntity crucible;
    
    public CrucibleRecipeInput(CrucibleBlockEntity crucible) {
        this.crucible = crucible;
    }
    
    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < 9) {
            return crucible.getItem(slot);
        }
        return ItemStack.EMPTY;
    }
    

    @Override
    public int size() {
        return 9;
    }
}