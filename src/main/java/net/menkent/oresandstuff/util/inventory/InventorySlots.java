package net.menkent.oresandstuff.util.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A very simple {@link Slot} util for making easy inventory slots
 *
 * @author Menkent
 */
public class InventorySlots {

    public class AbstractOutputSlot extends Slot {
        public AbstractOutputSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
