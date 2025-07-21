package net.dndats.daggersandthieves.entities.thief.ai;

import net.dndats.daggersandthieves.entities.thief.ThiefEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class AmbushGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final ThiefEntity mob;

    public AmbushGoal(ThiefEntity mob, Class targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (this.target != null) {
            double distanceToTarget = this.mob.distanceToSqr(target);
            boolean isBehind = this.mob.getDirection() == this.target.getDirection();

            boolean canUse = distanceToTarget < 6 && !isBehind;

            return super.canUse() && canUse;
        }
        return false;
    }

}
