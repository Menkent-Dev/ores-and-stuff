package net.menkent.oresandstuff.screen;

import net.menkent.oresandstuff.blockentity.CrucibleBlockEntity;
import net.menkent.oresandstuff.util.fuel.CrucibleFuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CrucibleScreenHandler extends AbstractContainerMenu{
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final CrucibleBlockEntity blockEntity;
    public final Player player;
    
    public CrucibleScreenHandler(int syncId, Inventory inventory, BlockPos pos) {
        this(
            syncId, 
            inventory, 
            inventory.player.level().getBlockEntity(pos), 
            new SimpleContainerData(11)
        );
    }

    public CrucibleScreenHandler(
        int syncId, 
        Inventory playerInventory, 
        BlockEntity blockEntity, 
        ContainerData arrayPropertyDelegate
    ) {
        super(ModScreens.CRUCIBLE_SCREEN_HANDLER, syncId);
        this.inventory = ((Container) blockEntity);
        this.blockEntity = ((CrucibleBlockEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;
        this.player = playerInventory.player;
        
        // if it works, it works
        this.addSlot(new Slot(inventory, 0, 44, 17));
        this.addSlot(new Slot(inventory, 1, 62, 17));
        this.addSlot(new Slot(inventory, 2, 80, 17));
        this.addSlot(new Slot(inventory, 3, 44, 35));
        this.addSlot(new Slot(inventory, 4, 62, 35));
        this.addSlot(new Slot(inventory, 5, 80, 35));
        this.addSlot(new Slot(inventory, 6, 44, 53));
        this.addSlot(new Slot(inventory, 7, 62, 53));
        this.addSlot(new Slot(inventory, 8, 80, 53));
        
        // Fuel and output slots
        this.addSlot(new Slot(inventory, 9, 8, 53));
        this.addSlot(new Slot(inventory, 10, 133, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack itemStack) {
                super.onTake(player, itemStack);
                awardExperience(player);
            }

            @Override
            public ItemStack safeTake(int minAmount, int maxAmount, Player player) {
                ItemStack result = super.safeTake(minAmount, maxAmount, player);
                if (!result.isEmpty()) {
                    awardExperience(player);
                }
                return result;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(arrayPropertyDelegate);
    }

    public void awardExperience(Player player) {
        blockEntity.awardStoredExperience(player);
    }
    
    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public boolean hasFuel() {
        return propertyDelegate.get(2) > 0;
    }

    public int getScaledArrowProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuelTime = this.propertyDelegate.get(2);
        int fuelDuration = this.propertyDelegate.get(3);
        int fuelPixelHeight = 32;

        if (fuelDuration == 0) {
            fuelDuration = 200;
        }
        return fuelTime * fuelPixelHeight / fuelDuration;
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            
            if (index == CrucibleBlockEntity.OUTPUT_SLOT) {
                // From output to inventory
                if (!this.moveItemStackTo(itemStack1, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index >= 11) {
                // From player inventory
                // Check if it's fuel
                if (CrucibleFuelRegistry.getFuelTime(itemStack1.getItem()) > 0) {
                    if (!this.moveItemStackTo(itemStack1, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, 11, 47, false)) {
                return ItemStack.EMPTY;
            }
            
            if (itemStack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, itemStack1);
        }
        
        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }
    
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}

