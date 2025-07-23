package net.dndats.daggersandthieves.registry;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = DaggersAndThieves.MODID)
public class ModCreativeTabs {

    @SubscribeEvent
    public static void test(EnchantmentLevelSetEvent event) {
    }

    @SubscribeEvent
    public static void addCreative(final BuildCreativeModeTabContentsEvent event) {
        // Add items to the creative tab
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.WOODEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.WOODEN_DAGGER.get().getDefaultInstance(), ModItems.STONE_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.STONE_DAGGER.get().getDefaultInstance(), ModItems.GOLDEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.GOLDEN_DAGGER.get().getDefaultInstance(), ModItems.IRON_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.IRON_DAGGER.get().getDefaultInstance(), ModItems.DIAMOND_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.DIAMOND_DAGGER.get().getDefaultInstance(), ModItems.NETHERITE_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DaggersAndThieves.MODID);

//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT_TABS

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

}
