package net.menkent.oresandstuff.util.fuel;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CrucibleFuelRegistry extends AbstractFuelRegistry{
    public CrucibleFuelRegistry() {
        super();
    }

    public CrucibleFuelRegistry(int time) {
        super(new HashMap<>(), time);
    }

    public void register() {
        registerFuel(Items.LAVA_BUCKET, 100);
        registerFuel(Items.BLAZE_ROD, 12);
        registerFuel(Items.FIRE_CHARGE, 8);
        registerFuel(Items.BLAZE_POWDER, 5);
    }
}
