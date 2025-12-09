package net.menkent.oresandstuff.util.fuel;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CrucibleFuelRegistry {
    private static final Map<Item, Integer> FUEL_TIMES = new HashMap<>();
    public static int time = 200;

    public static void registerFuel(Item item, int burnTime) {
        FUEL_TIMES.put(item, burnTime * time);
    }

    public static int getFuelTime(Item item) {
        return FUEL_TIMES.getOrDefault(item, 0);
    }

    public static void register() {
        registerFuel(Items.LAVA_BUCKET, 100);
        registerFuel(Items.BLAZE_ROD, 12);
        registerFuel(Items.FIRE_CHARGE, 8);
        registerFuel(Items.BLAZE_POWDER, 5);
    }
}
