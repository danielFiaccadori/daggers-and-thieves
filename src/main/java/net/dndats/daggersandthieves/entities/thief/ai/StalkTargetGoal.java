package net.dndats.daggersandthieves.entities.thief.ai;

import net.dndats.daggersandthieves.entities.thief.ThiefEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class StalkTargetGoal extends Goal {

    protected final ThiefEntity mob;
    protected final double speedModifier;
    protected final float maxStalkDistance;
    protected final float minStalkDistance;

    @Nullable
    LivingEntity target;

    @Nullable
    BlockPos hidingPos;

    protected int timeToRecalculatePath;
    protected final Random random = new Random();

    public StalkTargetGoal(ThiefEntity mob, double speedModifier, float minDistance, float maxDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.minStalkDistance = minDistance;
        this.maxStalkDistance = maxDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        //TODO: definir lógica para não executar com jogadores no criativo e alvejar também villagers
        this.target = this.mob.level().getNearestPlayer(this.mob, this.maxStalkDistance);

        if (this.target instanceof Player player && player.isCreative()) {
            return false;
        }

        if (this.target == null) {
            return false;
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        boolean canSee = this.target.hasLineOfSight(this.mob);
        boolean tooClose = this.mob.distanceToSqr(this.target) < this.minStalkDistance * this.minStalkDistance;
        boolean hasFinishedNavigating = this.mob.getNavigation().isDone();

        return !canSee && !tooClose && hasFinishedNavigating;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() &&
                !this.target.hasLineOfSight(this.mob) &&
                !this.mob.getNavigation().isDone() &&
                this.mob.distanceToSqr(this.target) > this.minStalkDistance * this.minStalkDistance;
    }

    @Override
    public void start() {
        this.findAndMoveToHidingSpot();
        this.mob.setSprinting(true);
        this.timeToRecalculatePath = 0;
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mob.setSprinting(false);
        this.target = null;
        this.hidingPos = null;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }

        this.timeToRecalculatePath--;
        if (this.timeToRecalculatePath <= 0) {
            this.timeToRecalculatePath = 20;
            this.findAndMoveToHidingSpot();
        }
    }

    private void findAndMoveToHidingSpot() {
        BlockPos newHidingSpot = this.searchForHidingSpot();
        if (newHidingSpot != null) {
            this.hidingPos = newHidingSpot;
            this.mob.getNavigation().moveTo(this.hidingPos.getX() + 0.5, this.hidingPos.getY(), this.hidingPos.getZ() + 0.5, this.speedModifier);
        }
    }

    @Nullable
    private BlockPos searchForHidingSpot() {
        if (this.target == null) {
            return null;
        }

        Level level = this.mob.level();
        List<BlockPos> potentialSpots = new ArrayList<>();
        int searchRadius = 16;

        for (int i = 0; i < 30; i++) {
            int x = this.target.getBlockX() + this.random.nextInt(searchRadius * 2) - searchRadius;
            int y = this.target.getBlockY() + this.random.nextInt(8) - 4;
            int z = this.target.getBlockZ() + this.random.nextInt(searchRadius * 2) - searchRadius;
            BlockPos potentialPos = new BlockPos(x, y, z);

            if (isHidingSpotValid(potentialPos)) {
                potentialSpots.add(potentialPos);
            }
        }

        if (!potentialSpots.isEmpty()) {
            return potentialSpots.get(this.random.nextInt(potentialSpots.size()));
        }

        return null;
    }

    private boolean isHidingSpotValid(BlockPos pos) {
        if (this.target == null) return false;
        Level level = this.mob.level();

        if (!level.getBlockState(pos.below()).isSolidRender(level, pos.below()) || !level.isEmptyBlock(pos) || !level.isEmptyBlock(pos.above())) {
            return false;
        }

        if (target instanceof Player player) {
            if (isPositionVisibleToPlayer(player, pos)) {
                return false;
            }
        }

        if (level.getBrightness(LightLayer.BLOCK, pos) > 7) {
            return false;
        }

        if (level.canSeeSky(pos)) {
            return false;
        }

        return true;
    }

    private boolean isPositionVisibleToPlayer(Player player, BlockPos pos) {
        Vec3 startVec = player.getEyePosition();
        Vec3 endVec = Vec3.atCenterOf(pos);

        ClipContext context = new ClipContext(
                startVec,
                endVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        );

        return this.mob.level().clip(context).getType() == BlockHitResult.Type.MISS;
    }

}
