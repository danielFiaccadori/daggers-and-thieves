package net.dndats.daggersandthieves.events;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.daggersandthieves.entities.thief.ThiefEntity;
import net.dndats.daggersandthieves.registry.ModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = DaggersAndThieves.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.THIEF.get(), ThiefEntity.createAttributes().build());
    }
}
