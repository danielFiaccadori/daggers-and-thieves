package net.dndats.daggersandthieves.entities.thief;

import net.dndats.daggersandthieves.entities.thief.ai.StalkTargetGoal;
import net.dndats.daggersandthieves.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class ThiefEntity extends AbstractIllager implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ThiefEntity(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.25D, false));
        this.goalSelector.addGoal(2, new StalkTargetGoal(this, 1.25D, 6.0F, 32.0F));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
    }

    @Override
    public void applyRaidBuffs(@NotNull ServerLevel serverLevel, int i, boolean b) {

    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_SPEED, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        RandomSource randomsource = level.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        this.populateDefaultEquipmentEnchantments(level, randomsource, difficulty);
        return spawngroupdata;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource random, @NotNull DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_DAGGER));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.IRON_DAGGER));
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 10, state -> {

            double velocity = this.getDeltaMovement().horizontalDistanceSqr();
            float speed = (float) (Math.sqrt(velocity) * 5.0);
            speed = Math.min(speed, 3.0F);

            if (state.isMoving()) {
                if (isAggressive() || isSprinting()) {
                    state.setControllerSpeed(speed);
                    return state.setAndContinue(RawAnimation.begin().thenLoop("sprint"));
                }
                return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }

            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));

        controllers.add(new AnimationController<>(this, "trigger_controller", 2, state -> PlayState.STOP)
                .triggerableAnim("attack_1", RawAnimation.begin().then("attack_1", Animation.LoopType.PLAY_ONCE))
                .triggerableAnim("attack_2", RawAnimation.begin().then("attack_2", Animation.LoopType.PLAY_ONCE))
        );
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof LivingEntity entity) {
            if (entity.getLastAttacker() != this) {
                level().playSound(target, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 1);
                this.triggerAnim("trigger_controller", "attack_2");
            } else {
                this.triggerAnim("trigger_controller", "attack_1");
            }
        }
        return super.doHurtTarget(target);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        super.travel(travelVector);
    }

}
