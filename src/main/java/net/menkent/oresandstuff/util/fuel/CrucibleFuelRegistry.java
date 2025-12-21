package net.menkent.oresandstuff.util.fuel;

import java.util.HashMap;
import net.minecraft.world.item.Items;

public class CrucibleFuelRegistry extends AbstractFuelRegistry{
    public CrucibleFuelRegistry() {
        super();
    }

    public CrucibleFuelRegistry(int time) {
        super(new HashMap<>(), time);
    }

    public void register() {
        registerFuel(Items.LAVA_BUCKET, 1000, 1);
        registerFuel(Items.BLAZE_ROD, 300, 1);
        registerFuel(Items.FIRE_CHARGE, 270, 1);
        registerFuel(Items.BLAZE_POWDER, 175, 1);
    }
}
