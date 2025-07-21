package net.dndats.daggersandthieves.registry;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.daggersandthieves.entities.thief.ThiefEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DaggersAndThieves.MODID);

    public static ResourceKey<EntityType<?>> THIEF_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("thief"));

    public static final Supplier<EntityType<ThiefEntity>> THIEF =
            ENTITIES.register("thief", () -> EntityType.Builder.of(ThiefEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).build(String.valueOf(THIEF_KEY)));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

}
