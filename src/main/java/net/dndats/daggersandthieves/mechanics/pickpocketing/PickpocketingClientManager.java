package net.dndats.daggersandthieves.mechanics.pickpocketing;

import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PickpocketingClientManager {

    private static final ConcurrentHashMap<UUID, Integer> pickpocketingProgress = new ConcurrentHashMap<>();
    private static final Integer finalPickpocketingProgress = 100;

    public static boolean hasFinishedPickpocketing(LivingEntity entity) {
        UUID entityUUID = entity.getUUID();
        return pickpocketingProgress.get(entityUUID) >= finalPickpocketingProgress;
    }

    public static void incrementPickpocketingProgress(LivingEntity entity, int increment) {
        UUID entityUUID = entity.getUUID();
        if (!pickpocketingProgress.containsKey(entityUUID)) pickpocketingProgress.put(entityUUID, 0);

        pickpocketingProgress.merge(entityUUID, increment, Integer::sum);
    }

    public static void removePickpocketing(LivingEntity entity) {
        UUID entityUUID = entity.getUUID();
        pickpocketingProgress.remove(entityUUID);
    }

}
