package net.menkent.oresandstuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.menkent.oresandstuff.block.ModBlocks;
import net.menkent.oresandstuff.blockentity.ModBlockEntities;
import net.menkent.oresandstuff.item.ModItemGroups;
import net.menkent.oresandstuff.item.ModItems;
import net.menkent.oresandstuff.recipe.ModRecipes;
import net.menkent.oresandstuff.screen.ModScreens;
import net.menkent.oresandstuff.util.fuel.ModFuelRegistry;
import net.menkent.oresandstuff.worldgen.ModWorldGeneration;

public class OresNStuff implements ModInitializer {
	public static final String MOD_ID = "ores_and_stuff";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing all mod packages");
		// idc if they're mad with this lmao
		LOGGER.info("No mixins detected, skipping");
		// utils first
		ModFuelRegistry.register();

		ModBlocks.register();
		ModItems.register();
		ModItemGroups.register();
		ModRecipes.register();
		ModBlockEntities.register();
		ModScreens.register();

		ModWorldGeneration.generateModWorldGen();

		LOGGER.info("All packages initialized");
	}
}