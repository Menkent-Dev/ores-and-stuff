package net.menkent.oresandstuff.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.menkent.oresandstuff.block.ModBlocks;
import net.minecraft.core.HolderLookup.Provider;

public class ModLootTableProvider extends FabricBlockLootTableProvider{

    protected ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.CRUCIBLE);
        dropSelf(ModBlocks.STEEL_BLOCK);
    }

}
