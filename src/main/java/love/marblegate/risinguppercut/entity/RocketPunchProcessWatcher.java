package love.marblegate.risinguppercut.entity;

import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.misc.LootUtil;
import love.marblegate.risinguppercut.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;


import java.util.List;

public class RocketPunchProcessWatcher extends Entity {
    static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(RocketPunchProcessWatcher.class, EntityDataSerializers.INT);
    int effectiveChargeTime;
    double dx;
    double dz;
    float damagePerEffectiveCharge;
    double speedIndex;
    double knockbackSpeedIndex;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    int shouldLoot;
    Player source;
    boolean stopTracking = false;


    public RocketPunchProcessWatcher(EntityType<? extends RocketPunchProcessWatcher> entityTypeIn, Level level) {
        super(entityTypeIn, level);
    }

    public RocketPunchProcessWatcher(Level level, BlockPos pos, int effectiveChargeTime, double knockbackSpeedIndex, double speedIndex, float damagePerEffectiveCharge, double dx, double dz, boolean ignoreArmor, boolean healing, boolean isFireDamage, Player source, int shouldLoot) {
        super(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
        entityData.set(TIMER, effectiveChargeTime);
        this.effectiveChargeTime = effectiveChargeTime;
        this.knockbackSpeedIndex = knockbackSpeedIndex;
        this.damagePerEffectiveCharge = damagePerEffectiveCharge;
        this.dx = dx;
        this.dz = dz;
        this.source = source;
        this.isFireDamage = isFireDamage;
        this.healing = healing;
        this.ignoreArmor = ignoreArmor;
        this.shouldLoot = shouldLoot;
        this.speedIndex = speedIndex;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            int temp = entityData.get(TIMER);

            //Deal with rocket punch is valid
            if (temp > 0 && !stopTracking && source != null) {
                //Slightly enlarge player's hitbox
                AABB collideBox = source.getBoundingBox().inflate(0.5f, 0, 0.5f);

                //Collision Detection
                List<LivingEntity> checks = level.getEntitiesOfClass(LivingEntity.class, collideBox);
                checks.remove(source);

                //If any mob is detected
                if (!checks.isEmpty()) {
                    // spawn an watchEntity to simulate rocket punch effect
                    RocketPunchImpactWatcher watchEntity = new RocketPunchImpactWatcher(level, source.blockPosition(), temp, effectiveChargeTime,
                            knockbackSpeedIndex, damagePerEffectiveCharge, dx, dz,
                            ignoreArmor, healing, isFireDamage, source);
                    for (LivingEntity target : checks) {
                        // Deal damage
                        DamageSource damageSource = new RocketPunchDamageSource(source);
                        if (shouldLoot > 0) {
                            LootUtil.dropLoot(target, DamageSource.playerAttack(source), true, source);
                        }
                        if (isFireDamage) {
                            damageSource.setIsFire();
                            target.setSecondsOnFire(3);
                            target.hurt(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        } else if (ignoreArmor) {
                            damageSource.bypassArmor();
                            target.hurt(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        } else if (healing) {
                            target.heal(damagePerEffectiveCharge * effectiveChargeTime);
                        } else {
                            target.hurt(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        }

                        if (target.isAlive()) {
                            watchEntity.watch(target);
                        }
                    }
                    source.level.addFreshEntity(watchEntity);

                    // Player stop moving and clear pocket punch status
                    source.setDeltaMovement(0, 0, 0);
                    source.hurtMarked = true;
                    stopTracking = true;
                }

                // If rocket punch is active and player hit a wall
                // stop player and clear rocket punch status
                if (source.horizontalCollision && !stopTracking) {
                    source.setDeltaMovement(0, 0, 0);
                    source.hurtMarked = true;
                    stopTracking = true;
                }

                // Deal with player rocket punch movement
                if (!stopTracking) {
                    // lock moving direction
                    source.setDeltaMovement(dx * speedIndex, 0.1, dz * speedIndex);
                    source.hurtMarked = true;
                    entityData.set(TIMER, temp - 1);
                }
            }

            if (stopTracking || source == null || temp == 0) {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
        source = null;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    protected void defineSynchedData() {
        entityData.define(TIMER, 0);
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
