package net.menkent.oresandstuff.util.fuel;

import net.menkent.oresandstuff.OresNStuff;

public class ModFuelRegistry {
    public static CrucibleFuelRegistry crucibleFuelRegistry = new CrucibleFuelRegistry();

    public static void register() {
        crucibleFuelRegistry.register();
        
        OresNStuff.LOGGER.info("Registering Fuel Registries");
    }
}
