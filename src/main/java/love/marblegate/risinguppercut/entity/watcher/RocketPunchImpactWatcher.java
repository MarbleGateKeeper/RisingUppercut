package love.marblegate.risinguppercut.entity.watcher;

import love.marblegate.risinguppercut.damagesource.RocketPunchOnWallDamageSource;
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
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;


public class RocketPunchImpactWatcher extends Entity {
    static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(RocketPunchImpactWatcher.class, EntityDataSerializers.INT);
    int effectiveChargeTime;
    double knockbackSpeedIndex;
    float damagePerEffectiveCharge;
    double dx;
    double dz;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    Player source;
    List<YUnchangedLivingEntity> watchedEntities;


    public RocketPunchImpactWatcher(EntityType<? extends RocketPunchImpactWatcher> entityTypeIn, Level level) {
        super(entityTypeIn, level);
    }

    public RocketPunchImpactWatcher(Level level, BlockPos pos, int timer, int effectiveChargeTime, double knockbackSpeedIndex, float damagePerEffectiveCharge, double dx, double dz, boolean ignoreArmor, boolean healing, boolean isFireDamage, Player source) {
        super(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
        entityData.set(TIMER, timer);
        this.effectiveChargeTime = effectiveChargeTime;
        this.knockbackSpeedIndex = knockbackSpeedIndex;
        this.damagePerEffectiveCharge = damagePerEffectiveCharge;
        this.dx = dx;
        this.dz = dz;
        this.source = source;
        this.isFireDamage = isFireDamage;
        this.healing = healing;
        this.ignoreArmor = ignoreArmor;
        watchedEntities = new ArrayList<>();
    }

    public void watch(LivingEntity livingEntity) {
        if (livingEntity != null) {
            watchedEntities.add(new YUnchangedLivingEntity(livingEntity));
        }
    }

    public void removeFromWatchList(YUnchangedLivingEntity yUnchangedLivingEntity) {
        if (yUnchangedLivingEntity != null) {
            watchedEntities.remove(yUnchangedLivingEntity);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            int temp = entityData.get(TIMER);
            if (watchedEntities != null && source != null) {
                if (!watchedEntities.isEmpty()) {
                    List<YUnchangedLivingEntity> entitiesRemoveFromWatchList = new ArrayList<>();
                    for (YUnchangedLivingEntity entity : watchedEntities) {
                        if (entity.livingEntity.horizontalCollision) {
                            DamageSource damageSource = new RocketPunchOnWallDamageSource(source);
                            float realDamageApplied = damagePerEffectiveCharge * effectiveChargeTime + 1;
                            if (effectiveChargeTime - temp < 10) realDamageApplied = realDamageApplied * 2 - 1;
                            {
                                if (ignoreArmor) {
                                    damageSource.bypassArmor();
                                    entity.livingEntity.hurt(damageSource, realDamageApplied);
                                } else if (isFireDamage) {
                                    damageSource.setIsFire();
                                    entity.livingEntity.hurt(damageSource, realDamageApplied);
                                } else if (healing) {
                                    entity.livingEntity.heal(damagePerEffectiveCharge);
                                } else {
                                    entity.livingEntity.hurt(damageSource, realDamageApplied);
                                }
                            }
                            entitiesRemoveFromWatchList.add(entity);
                        } else {
                            entity.setMotion(dx * knockbackSpeedIndex, dz * knockbackSpeedIndex);
                        }
                    }
                    for (YUnchangedLivingEntity remove : entitiesRemoveFromWatchList) {
                        removeFromWatchList(remove);
                    }
                    if (temp - 1 == 0) {
                        watchedEntities.clear();
                        remove(RemovalReason.DISCARDED);
                    } else entityData.set(TIMER, temp - 1);
                } else {
                    if (temp - 1 == 0) remove(RemovalReason.DISCARDED);
                    else entityData.set(TIMER, temp - 1);
                }
            } else {
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

    static class YUnchangedLivingEntity {
        LivingEntity livingEntity;
        double Y;

        public YUnchangedLivingEntity(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
            Y = livingEntity.getY();
        }

        void setMotion(double X, double Z) {
            livingEntity.setDeltaMovement(X, 0, Z);
            livingEntity.setPos(livingEntity.getX(), Y, livingEntity.getZ());
            livingEntity.hurtMarked = true;
        }

    }
}
