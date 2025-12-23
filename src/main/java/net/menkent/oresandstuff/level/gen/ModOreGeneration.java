package net.menkent.oresandstuff.level.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.menkent.oresandstuff.level.gen.feature.ModPlacedFeatures;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModOreGeneration {
    public static void generateOres() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(), 
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ModPlacedFeatures.TITANIUM_ORE_PLACED_KEY
        );

        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(), 
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ModPlacedFeatures.DEEPSLATE_TITANIUM_ORE_PLACED_KEY
        );
    }
}
