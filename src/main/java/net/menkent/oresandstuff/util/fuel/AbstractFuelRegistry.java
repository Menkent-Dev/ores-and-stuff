package net.menkent.oresandstuff.util.fuel;

import java.util.Collections;
import java.util.HashMap;
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

    public AbstractFuelRegistry() {
        this(new HashMap<>(), 200);
    }

    public AbstractFuelRegistry(Map<Item, Integer> fuelMap, int time) {
        AbstractFuelRegistry.fuelMap = new HashMap<>(fuelMap);
        AbstractFuelRegistry.time = time;
    }

    /**
     * Registers fuel into the registry.
     * 
     * @param item
     * @param burnTime
     */
    public void registerFuel(Item item, int burnTime) {
        fuelMap.put(item, burnTime * time);
    }

    /**
     * Retrieves the fuel time of the specified item
     * 
     * @param item
     * @param burnTime
     */
    public int getFuelTime(Item item) {
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

    /**
     * Returns a fuel map.
     */
    public Map<Item, Integer> getFuelMap() {
        return Collections.unmodifiableMap(fuelMap);
    }
}
