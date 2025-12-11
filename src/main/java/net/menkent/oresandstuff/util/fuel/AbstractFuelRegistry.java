package net.menkent.oresandstuff.util.fuel;

import java.util.Map;

import net.minecraft.world.item.Item;

/**
 * A very simple abstract fuel registry for making very easy fuel registries.
 *
 * @author Menkent
 */
public abstract class AbstractFuelRegistry {
    public static Map<Item, Integer> fuelMap;
    public static int time;

    public AbstractFuelRegistry(Map<Item, Integer> fuelMap) {
        this(fuelMap, 200);
    }

    public AbstractFuelRegistry(Map<Item, Integer> fuelMap, int time) {
        AbstractFuelRegistry.fuelMap = fuelMap;
        AbstractFuelRegistry.time = time;
    }

    /**
     * Registers fuel into the registry.
     * 
     * @param item
     * @param burnTime
     */
    public static void registerFuel(Item item, int burnTime) {
        fuelMap.put(item, burnTime * time);
    }

    /**
     * Retrieves the fuel time of the specified item
     * 
     * @param item
     * @param burnTime
     */
    public static int getFuelTime(Item item) {
        return fuelMap.getOrDefault(item, 0);
    }

    /**
     * Checks if the fuel item exists in the registry.
     * 
     * @param item
     * @param burnTime
     */
    public boolean isFuel(Item item) {
        return getFuelTime(item) > 0;
    }
}
