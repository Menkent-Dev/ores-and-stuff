package net.menkent.oresandstuff.util.fuel;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CrucibleFuelRegistry {
    private static final Map<Item, Integer> FUEL_TIMES = new HashMap<>();

    public static void registerFuel(Item item, int burnTime) {
        FUEL_TIMES.put(item, burnTime);
    }

    public static int getFuelTime(Item item) {
        return FUEL_TIMES.getOrDefault(item, 0);
    }

    public static void register() {
        registerFuel(Items.LAVA_BUCKET, 20000);
    }
}
