package net.menkent.oresandstuff.util.fuel;

import net.menkent.oresandstuff.OresNStuff;

public class ModFuelRegistry {
    public static void register() {
        CrucibleFuelRegistry.register();
        OresNStuff.LOGGER.info("Registering Fuel Registries");
    }
}
