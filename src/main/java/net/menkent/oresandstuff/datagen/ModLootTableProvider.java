package net.menkent.oresandstuff.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.menkent.oresandstuff.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableProvider extends FabricBlockLootTableProvider{

    protected ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.CRUCIBLE);
        dropSelf(ModBlocks.STEEL_BLOCK);
        add(ModBlocks.TITANIUM_ORE, multipleOreDrops(ModBlocks.TITANIUM_ORE, Items.RAW_COPPER, 2, 4));
        add(ModBlocks.DEEPSLATE_TITANIUM_ORE, multipleOreDrops(ModBlocks.DEEPSLATE_TITANIUM_ORE, Items.RAW_COPPER, 3, 5));
    }

    public LootTable.Builder multipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
            block, 
            // bruh wtf is this?
            this.applyExplosionDecay(
                block, 
                (
                    (LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(item)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                ).apply(ApplyBonusCount.addOreBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
        ));
    }
}
