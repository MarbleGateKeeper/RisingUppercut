package love.marblegate.risinguppercut.entity;

import love.marblegate.risinguppercut.damagesource.RisingUppercutDamageSource;
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
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;


public class RisingUppercutWatcher extends Entity {
    static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(RisingUppercutWatcher.class, EntityDataSerializers.INT);
    int totalTime;
    int floatingTime;
    double speedIndex;
    float damage;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    Player source; //Can be null
    List<LivingEntity> watchedEntities;

    public RisingUppercutWatcher(Level level, BlockPos pos, Player source, int upwardTime, int floatingTime, double speedIndex, float damage, boolean ignoreArmor, boolean healing, boolean isFireDamage) {
        super(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
        entityData.set(TIMER, upwardTime + floatingTime);
        totalTime = upwardTime + floatingTime;
        this.source = source;
        this.floatingTime = floatingTime;
        this.speedIndex = speedIndex;
        this.damage = damage;
        this.ignoreArmor = ignoreArmor;
        this.healing = healing;
        this.isFireDamage = isFireDamage;
        watchedEntities = new ArrayList<>();
    }

    public RisingUppercutWatcher(EntityType<? extends RisingUppercutWatcher> entityTypeIn, Level level) {
        super(entityTypeIn, level);
    }

    public void watch(LivingEntity livingEntity) {
        if (livingEntity != null) {
            watchedEntities.add(livingEntity);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide()) {
            int temp = entityData.get(TIMER);
            if (watchedEntities != null && source != null) {
                if (!watchedEntities.isEmpty()) {
                    for (LivingEntity entity : watchedEntities) {
                        if (temp == totalTime - 1) {
                            DamageSource damageSource = new RisingUppercutDamageSource(source);
                            if (isFireDamage) {
                                damageSource.setIsFire();
                                entity.hurt(damageSource, damage);
                                entity.setSecondsOnFire(3);
                            } else if (ignoreArmor) {
                                damageSource.bypassArmor();
                                entity.hurt(damageSource, damage);
                            } else if (healing) {
                                System.out.print(entity.getHealth() + "->");
                                entity.heal(damage);
                                System.out.print(entity.getHealth() + "\n");
                            } else {
                                entity.hurt(damageSource, damage);
                            }
                        }
                        if (temp > floatingTime) {
                            moveVertically(entity, temp * speedIndex);
                        } else if (temp > floatingTime / 2) {
                            moveVertically(entity, 0.1);
                        } else {
                            moveVertically(entity, -0.1);
                        }
                    }
                }
                if (temp > floatingTime) {
                    moveVertically(source, temp * speedIndex);
                } else if (temp > floatingTime / 2) {
                    moveVerticallyWithHorizonControl(source, 0.1);
                } else {
                    moveVerticallyWithHorizonControl(source, -0.1);
                }
                if (temp - 1 == 0) remove(RemovalReason.DISCARDED);
                else entityData.set(TIMER, temp - 1);
            } else {
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    void moveVertically(LivingEntity livingEntity, double speed) {
        livingEntity.setDeltaMovement(0, speed, 0);
        livingEntity.hurtMarked = true;
    }

    void moveVerticallyWithHorizonControl(LivingEntity livingEntity, double speed) {
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().x(), speed, livingEntity.getDeltaMovement().z());
        livingEntity.hurtMarked = true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
        source = null;
        entityData.set(TIMER, 0);
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
