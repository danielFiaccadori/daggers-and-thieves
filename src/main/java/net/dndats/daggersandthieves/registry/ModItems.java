package net.dndats.daggersandthieves.registry;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.daggersandthieves.item.DaggerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, DaggersAndThieves.MODID);

    // Daggers
    public static final DeferredHolder<Item, Item> WOODEN_DAGGER =
            ITEMS.register("wooden_dagger", () -> new DaggerItem(Tiers.WOOD, 1F, -2F, new Item.Properties()));
    public static final DeferredHolder<Item, Item> STONE_DAGGER =
            ITEMS.register("stone_dagger", () -> new DaggerItem(Tiers.STONE, 1.5F, -2F, new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLDEN_DAGGER =
            ITEMS.register("golden_dagger", () -> new DaggerItem(Tiers.GOLD, 1F, -2F, new Item.Properties()));
    public static final DeferredHolder<Item, Item> IRON_DAGGER =
            ITEMS.register("iron_dagger", () -> new DaggerItem(Tiers.IRON, 2F, -2F, new Item.Properties()));
    public static final DeferredHolder<Item, Item> DIAMOND_DAGGER =
            ITEMS.register("diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, 2.5F, -2F, new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_DAGGER =
            ITEMS.register("netherite_dagger", () -> new DaggerItem(Tiers.NETHERITE, 3F, -2F, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
