package net.menkent.oresandstuff.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class TagUtil {

    /**
     * Get all items in a tag from {@link Level}.
     */
    public static List<Item> getItemsInTag(Level level, TagKey<Item> tag) {
        HolderLookup.RegistryLookup<Item> itemRegistry = level.registryAccess()
            .lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);
        
        return itemRegistry.get(tag)
            .map(holders -> holders.stream()
                .map(holder -> holder.value())
                .collect(Collectors.toList()))
            .orElse(List.of());
    }
    
    /**
     * Get all items in a tag from {@link RegistryAccess}.
     */
    public static List<Item> getItemsInTag(RegistryAccess registryAccess, TagKey<Item> tag) {
        HolderLookup.RegistryLookup<Item> itemRegistry = registryAccess
            .lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);
        
        return itemRegistry.get(tag)
            .map(holders -> holders.stream()
                .map(holder -> holder.value())
                .collect(Collectors.toList()))
            .orElse(List.of());
    }
    
    /**
     * Check if an item is in a tag.
     */
    public static boolean isItemInTag(Level level, Item item, TagKey<Item> tag) {
        HolderLookup.RegistryLookup<Item> itemRegistry = level.registryAccess()
            .lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);
        
        return itemRegistry.get(tag)
            .map(holders -> holders.stream()
                .anyMatch(holder -> holder.value() == item))
            .orElse(false);
    }
}