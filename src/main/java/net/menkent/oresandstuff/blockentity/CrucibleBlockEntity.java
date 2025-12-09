package net.menkent.oresandstuff.blockentity;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.authlib.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.menkent.oresandstuff.block.CrucibleBlock;
import net.menkent.oresandstuff.recipe.CrucibleRecipe;
import net.menkent.oresandstuff.screen.CrucibleScreenHandler;
import net.menkent.oresandstuff.util.fuel.CrucibleFuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class CrucibleBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);

	public static final int TOTAL_SLOTS = 11;
    public static final int CRAFTING_SLOTS = 9;
    public static final int FUEL_SLOT = 9;
    public static final int OUTPUT_SLOT = 10;


	protected final ContainerData propertyDelegate;
	public int progress = 0;
	public int maxProgress = 240; // 20ticks/sec = 12 sec
	public int fuelTime = 0;
	public int fuelDuration = 0;
	public float storedExperience = 0.0f;
	public int recipesCraftedSinceLastCollection = 0;

	private int particleCooldown = 0;

	BlockPos pos;
	Level world;

	public CrucibleBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRUCIBLE_BLOCK_ENTITY, pos, state);
		this.propertyDelegate = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CrucibleBlockEntity.this.progress;
					case 1 -> CrucibleBlockEntity.this.maxProgress;
					case 2 -> CrucibleBlockEntity.this.fuelTime;
					case 3 -> CrucibleBlockEntity.this.fuelDuration;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0: CrucibleBlockEntity.this.progress = value;
					case 1: CrucibleBlockEntity.this.maxProgress = value;
					case 2: CrucibleBlockEntity.this.fuelTime = value;
					case 3: CrucibleBlockEntity.this.fuelDuration = value;
				}
			}

			@Override
			public int getCount() {
				return 4;
			}
		};
	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayer player) {
		return this.worldPosition;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.ores_and_stuff.crucible_block");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
		return new CrucibleScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	@Override
	protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view, inventory);
        view.putInt("crucible_block.progress", progress);
		view.putInt("crucible_block.max_progress", maxProgress);
		view.putInt("crucible_block.fuel_time", fuelTime);
        view.putInt("crucible_block.fuel_duration", fuelDuration);
		view.putFloat("crucible_block.stored_experience", storedExperience);
		view.putInt("crucible_block.recipes_crafted_since_last_collection", recipesCraftedSinceLastCollection);
    }

	@Override
	protected void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
        ContainerHelper.loadAllItems(view, inventory);
		progress = view.getIntOr("crucible_block.progress", 0 );
		maxProgress = view.getIntOr("crucible_block.max_progress", 0);
		fuelTime = view.getIntOr("crucible_block.fuel_time", 0);
        fuelDuration = view.getIntOr("crucible_block.fuel_duration", 0);
		storedExperience = view.getFloatOr("crucible_block.stored_experience", 0f);
		recipesCraftedSinceLastCollection = view.getIntOr("crucible_block.recipes_crafted_since_last_collection", 0);

    }

	public static int getCraftingSlot(int row, int column) {
        return row * 3 + column; // 0-8 for 3x3 grid
    }

	private boolean isBurning() {
        return fuelTime > 0;
    }

	private boolean canConsumeFuel() {
        ItemStack fuelStack = this.getItem(FUEL_SLOT);
        return CrucibleFuelRegistry.getFuelTime(fuelStack.getItem()) > 0 && fuelStack.getCount() > 0 && fuelTime <= 0;
    }

	private void consumeFuel() {
        if (canConsumeFuel()) {
            ItemStack fuelStack = this.getItem(FUEL_SLOT);
            int fuelValue = CrucibleFuelRegistry.getFuelTime(fuelStack.getItem());
            
            if (fuelValue > 0) {
                fuelTime = fuelValue;
                fuelDuration = fuelValue;
                if (fuelStack.getItem() == Items.LAVA_BUCKET) {
                    this.setItem(FUEL_SLOT, new ItemStack(Items.BUCKET));
                } else {
                    this.removeItem(FUEL_SLOT, 1);
                }
                
                setChanged();
            }
        }
	}

	public void tick(Level world, BlockPos pos, BlockState state) {
		boolean lit = !hasCraftingFinished();
		boolean wasBurning = isBurning();

		if (isBurning()) {
			Optional<RecipeHolder<CrucibleRecipe>> recipeHolder = CrucibleRecipe.getRecipeHolderFor(this, level);
			ItemStack currentOutput;

			if (recipeHolder.isPresent()) {
                CrucibleRecipe recipe = recipeHolder.get().value();
                
                if (this.progress == 0) {
                    this.maxProgress = recipe.getCookingTime();
				}
            }
			
			if (world.isClientSide()) {
				spawnParticles(world, pos, state);
			}

			if (!world.isClientSide() && hasRecipe()) {
				fuelTime--;
				increaseCraftingProgress();
				setChanged(world, pos, state);
	
				if (hasCraftingFinished()) {
					craftItem();
					resetProgress();
				}
			} else {
				resetProgress();
			}
		}

		if (!isBurning() && canConsumeFuel()) {
            consumeFuel();
        }

		if (state.getValue(CrucibleBlock.LIT) != lit) {
			world.setBlock(pos, state.setValue(CrucibleBlock.LIT, hasCraftingFinished()), Block.UPDATE_CLIENTS);
		}

		boolean isBurning = isBurning();
        if (wasBurning != isBurning) {
            world.setBlock(pos, state.setValue(CrucibleBlock.LIT, isBurning), Block.UPDATE_CLIENTS);
            setChanged(world, pos, state);
    	}
	}

	// TODO put this in lib
	private void spawnParticles(Level world, BlockPos pos, BlockState state) {
        // Reduce particle frequency with cooldown
        particleCooldown--;
        if (particleCooldown > 0) return;
        particleCooldown = 2; // Spawn particles every 3 ticks
        
        RandomSource random = world.random;
        
        // Base position (center of block)
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.8;
        double centerZ = pos.getZ() + 0.5;
        
        for (int i = 0; i < 2; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.4;
            double offsetZ = (random.nextDouble() - 0.5) * 0.4;
            
            world.addParticle(ParticleTypes.FLAME, 
                centerX + offsetX, 
                centerY + random.nextDouble() * 0.2, 
                centerZ + offsetZ, 
                0, 0.01, 0);
        }
        
        // Occasionally spawn smoke particles
        if (random.nextInt(5) == 0) {
            double smokeX = centerX + (random.nextDouble() - 0.5) * 0.3;
            double smokeZ = centerZ + (random.nextDouble() - 0.5) * 0.3;
            
            world.addParticle(ParticleTypes.SMOKE, smokeX, centerY + 0.1, smokeZ, 0, 0.05, 0);
        }
        
        // Spawn lava particles when actively crafting
        if (hasRecipe() && random.nextInt(3) == 0) {
            double lavaX = centerX + (random.nextDouble() - 0.5) * 0.2;
            double lavaZ = centerZ + (random.nextDouble() - 0.5) * 0.2;
            
            world.addParticle(ParticleTypes.LAVA, lavaX, centerY - 0.2, lavaZ, 0, 0, 0);
        }
    }

	private void resetProgress() {
		this.progress = 0;
		this.maxProgress = 240;
	}

	private void craftItem() {
        CrucibleRecipe recipe = CrucibleRecipe.getRecipeFor(this, level);
        if (recipe == null) return;
        
        ItemStack result = recipe.getResultItem();
        recipe.consumeInputs(this);
        
        ItemStack currentOutput = getItem(CrucibleBlockEntity.OUTPUT_SLOT);
        if (currentOutput.isEmpty()) {
            setItem(CrucibleBlockEntity.OUTPUT_SLOT, result.copy());
        } else if (currentOutput.is(result.getItem())) {
            currentOutput.grow(result.getCount());
        }
        
        this.maxProgress = progress;
        
        float recipeExperience = recipe.getExperience();
        if (recipeExperience > 0) {
            this.storedExperience += recipeExperience;
            this.recipesCraftedSinceLastCollection++;
            setChanged();
        }
    }

	public void awardStoredExperience(Player player) {
		// if it works, it works
		Vec3 playerPos = new Vec3(player.getX(), player.getY() ,player.getZ());

        if (this.storedExperience > 0 && !player.level().isClientSide) {
            int xpToAward = Mth.floor(this.storedExperience);
            float fractionalPart = this.storedExperience - xpToAward;
            
            if (xpToAward > 0) {
                ExperienceOrb.award(player.getServer().getLevel(player.level().dimension()), playerPos, xpToAward);
            }
            
            if (player.level().random.nextFloat() < fractionalPart) {
                ExperienceOrb.award(player.getServer().getLevel(player.level().dimension()), CrucibleBlockEntity.this.getBlockPos().getCenter(), 1);
            }
            
            this.storedExperience = 0.0f;
            this.recipesCraftedSinceLastCollection = 0;
            setChanged();
        }
    }

	private boolean hasCraftingFinished() {
		return this.progress >= this.maxProgress;
	}

	private void increaseCraftingProgress() {
		this.progress++;
	}

	private boolean hasRecipe() {
		CrucibleRecipe recipe = CrucibleRecipe.getRecipeFor(this, level);
        if (recipe == null) return false;
        
        ItemStack output = recipe.getResultItem();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output.getItem());
	}
	
	private boolean canInsertItemIntoOutputSlot(Item output) {
		return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getItem() == output.asItem();
	}

	private boolean canInsertAmountIntoOutputSlot(int count) {
		int maxCount = this.getItem(OUTPUT_SLOT).isEmpty() ? 64 : this.getItem(OUTPUT_SLOT).getMaxStackSize();
		int currentCount = this.getItem(OUTPUT_SLOT).getCount();

		return maxCount >= currentCount + count;
	}

	public float getFuelProgress() {
        if (fuelDuration == 0) {
            return 0;
        }
        return (float) fuelTime / fuelDuration;
    }

	public float getStoredExperience() {
        return this.storedExperience;
    }
    
    public int getRecipesCraftedSinceLastCollection() {
        return this.recipesCraftedSinceLastCollection;
    }

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}
}
