package net.menkent.oresandstuff.level.gen.feature;

import java.util.List;

import net.menkent.oresandstuff.OresNStuff;
import net.menkent.oresandstuff.util.levelgen.OrePlacementHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> TITANIUM_ORE_PLACED_KEY = registerKey("titanium_ore_placed");
    public static final ResourceKey<PlacedFeature> DEEPSLATE_TITANIUM_ORE_PLACED_KEY = registerKey("deepslate_titanium_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(
            context, 
            TITANIUM_ORE_PLACED_KEY, 
            configuredFeatures.getOrThrow(ModConfiguredFeatures.TITANIUM_ORE_KEY), 
            OrePlacementHelper.modifiersWithCount(9, HeightRangePlacement.triangle(VerticalAnchor.absolute(15), VerticalAnchor.absolute(45)))
        );

        register(
            context, 
            DEEPSLATE_TITANIUM_ORE_PLACED_KEY, 
            configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEPSLATE_TITANIUM_ORE_KEY), 
            OrePlacementHelper.modifiersWithCount(5, HeightRangePlacement.uniform(VerticalAnchor.absolute(-55), VerticalAnchor.absolute(-35)))
        );
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstrapContext<PlacedFeature> context, 
        ResourceKey<PlacedFeature> key,
        Holder<ConfiguredFeature<?, ?>> configuration,
        PlacementModifier... modifiers
    ) {
        register(context, key, configuration, List.of(modifiers));
    }

    private static void register(BootstrapContext<PlacedFeature> context,
        ResourceKey<PlacedFeature> key,
        Holder<ConfiguredFeature<?, ?>> configuration,
        List<PlacementModifier> modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
