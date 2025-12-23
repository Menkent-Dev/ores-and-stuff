package net.menkent.oresandstuff.item;

import java.util.function.Function;

import net.menkent.oresandstuff.OresNStuff;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item STEEL_INGOT = register(
		"steel_ingot", 
		Item::new, 
		new Item.Properties()
	);

	public static final Item TITANIUM_INGOT = register(
		"titanium_ingot", 
		Item::new, 
		new Item.Properties()
	);

	public static final Item RAW_TITANIUM = register(
		"raw_titanium", 
		Item::new, 
		new Item.Properties()
	);

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OresNStuff.MOD_ID, name));
		Item item = itemFactory.apply(settings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

    public static void register() {
        OresNStuff.LOGGER.info("Registering Mod Items");
    }
}
