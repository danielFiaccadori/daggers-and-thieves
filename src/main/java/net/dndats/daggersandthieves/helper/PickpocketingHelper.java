package net.dndats.daggersandthieves.helper;

import net.dndats.daggersandthieves.DaggersAndThieves;
import net.dndats.hackersandslashers.utils.helper.EntityHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PickpocketingHelper {

    private static final int PICKPOCKETING_RANGE = 2;

    public static boolean canPickpocket(LivingEntity victim, Player thief) {
        if (victim instanceof Mob mob) {
            boolean crouching = thief.isCrouching();
            boolean emptyHands = thief.getMainHandItem().isEmpty() && thief.getOffhandItem().isEmpty();

            if (!emptyHands) thief.displayClientMessage(Component.translatable("Your hands must be empty to pickpocket"), true);

            return crouching && emptyHands;
        }
        return false;
    }

    public static Optional<LivingEntity> getCurrentPickpocketing(Player thief) {
        Optional<Entity> targetEntity = performExtendedRaycast(thief);
        if (targetEntity.isPresent() && targetEntity.get() instanceof LivingEntity livingEntity) {
            return Optional.of(livingEntity);
        }

        return Optional.empty();
    }

    public static boolean isInsidePickpocketRange(LivingEntity victim, Player thief) {
        if (victim == null || thief == null) return false;

        Optional<Entity> targetEntity = performExtendedRaycast(thief);
        return targetEntity.isPresent() && targetEntity.get().is(victim);
    }

    private static Optional<Entity> performExtendedRaycast(Player player) {
        Vec3 startPos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPos = startPos.add(lookVec.scale(PICKPOCKETING_RANGE));

        Level level = player.level();

        BlockHitResult blockHitResult = level.clip(new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        AABB searchBox = player.getBoundingBox()
                .expandTowards(lookVec.scale(PICKPOCKETING_RANGE))
                .inflate(1.0D);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                level,
                player,
                startPos,
                endPos,
                searchBox,
                (entity) -> !entity.isSpectator() && entity.isPickable() && entity != player
        );

        if (entityHitResult == null) {
            return Optional.empty();
        }

        double blockDist = startPos.distanceTo(blockHitResult.getLocation());
        double entityDist = startPos.distanceTo(entityHitResult.getLocation());

        if (entityDist < blockDist) {
            return Optional.of(entityHitResult.getEntity());
        }

        return Optional.empty();
    }

}
