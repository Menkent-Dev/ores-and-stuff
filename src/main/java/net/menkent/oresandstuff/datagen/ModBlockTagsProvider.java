package net.menkent.oresandstuff.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider{

    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
        getOrCreateRawBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "steel_block"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "titanium_block"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "crucible"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "titanium_ore"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "deepslate_titanium_ore"));

        getOrCreateRawBuilder(BlockTags.NEEDS_IRON_TOOL)
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "steel_block"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "titanium_block"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "crucible"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "titanium_ore"))
            .addElement(ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, "deepslate_titanium_ore"));

    }


}
