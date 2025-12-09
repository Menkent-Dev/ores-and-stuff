package net.menkent.oresandstuff.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.menkent.oresandstuff.OresNStuff;

public class OresNStuffDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		OresNStuff.LOGGER.info("Initializing Data Genarator");
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		OresNStuff.LOGGER.info("Datagen Initialized");
	}
}
