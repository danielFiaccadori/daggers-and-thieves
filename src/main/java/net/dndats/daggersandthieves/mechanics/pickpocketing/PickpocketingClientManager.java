package net.dndats.daggersandthieves.mechanics.pickpocketing;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class PickpocketingClientManager {

    private static final ConcurrentHashMap<UUID, Integer> pickpocketingProgress = new ConcurrentHashMap<>();
    private static final Integer finalPickpocketingProgress = 40;

    public static boolean hasFinishedPickpocketing(LivingEntity entity) {
        return pickpocketingProgress.get(entity.getUUID()) >= finalPickpocketingProgress;
    }

    public static void incrementPickpocketingProgress(LivingEntity entity, int increment) {
        if (!pickpocketingProgress.containsKey(entity.getUUID())) pickpocketingProgress.put(entity.getUUID(), 0);
        pickpocketingProgress.merge(entity.getUUID(), increment, Integer::sum);
    }

    public static void removePickpocketing(LivingEntity entity) {
        pickpocketingProgress.remove(entity.getUUID());
    }

}
