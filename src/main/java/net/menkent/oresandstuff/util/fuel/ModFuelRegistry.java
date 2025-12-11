package net.menkent.oresandstuff.util.fuel;

import java.util.HashMap;
import java.util.Map;

import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.world.item.Item;

public class ModFuelRegistry {
    public static final Map<Item, Integer> fuelMap = new HashMap<>();
    
    public static void register() {
        CrucibleFuelRegistry crucibleFuelRegistry = new CrucibleFuelRegistry(fuelMap);
        crucibleFuelRegistry.register();
        
        OresNStuff.LOGGER.info("Registering Fuel Registries");
    }
}
