package love.marblegate.risinguppercut.entity.watcher;

import love.marblegate.risinguppercut.damagesource.RisingUppercutDamageSource;
import love.marblegate.risinguppercut.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;


public class RisingUppercutWatcher extends Entity {
    static final DataParameter<Integer> TIMER = EntityDataManager.createKey(RisingUppercutWatcher.class, DataSerializers.VARINT);
    int totalTime;
    int floatingTime;
    double speedIndex;
    float damage;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    PlayerEntity source; //Can be null
    List<LivingEntity> watchedEntities;

    public RisingUppercutWatcher(World worldIn, BlockPos pos, PlayerEntity source, int upwardTime, int floatingTime, double speedIndex, float damage, boolean ignoreArmor, boolean healing, boolean isFireDamage) {
        super(EntityRegistry.ROCKET_PUNCH_WATCHER.get(), worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        dataManager.set(TIMER, upwardTime + floatingTime);
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

    public RisingUppercutWatcher(EntityType<? extends RisingUppercutWatcher> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public void watch(LivingEntity livingEntity) {
        if (livingEntity != null) {
            watchedEntities.add(livingEntity);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote()) {
            int temp = dataManager.get(TIMER);
            if (watchedEntities != null && source != null) {
                if (!watchedEntities.isEmpty()) {
                    for (LivingEntity entity : watchedEntities) {
                        if (temp == totalTime - 1) {
                            DamageSource damageSource = new RisingUppercutDamageSource(source);
                            if (isFireDamage) {
                                damageSource.setFireDamage();
                                entity.attackEntityFrom(damageSource, damage);
                                entity.setFire(3);
                            } else if (ignoreArmor) {
                                damageSource.setDamageBypassesArmor();
                                entity.attackEntityFrom(damageSource, damage);
                            } else if (healing) {
                                System.out.print(entity.getHealth() + "->");
                                entity.heal(damage);
                                System.out.print(entity.getHealth() + "\n");
                            } else {
                                entity.attackEntityFrom(damageSource, damage);
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
                if (temp - 1 == 0) remove();
                else dataManager.set(TIMER, temp - 1);
            } else {
                remove();
            }
        }
    }

    void moveVertically(LivingEntity livingEntity, double speed) {
        livingEntity.setMotion(0, speed, 0);
        livingEntity.markPositionDirty();
        livingEntity.velocityChanged = true;
    }

    void moveVerticallyWithHorizonControl(LivingEntity livingEntity, double speed) {
        livingEntity.setMotion(livingEntity.getMotion().getX(), speed, livingEntity.getMotion().getZ());
        livingEntity.markPositionDirty();
        livingEntity.velocityChanged = true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }


    @Override
    protected void registerData() {
        dataManager.register(TIMER, 0);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        source = null;
        dataManager.set(TIMER, 0);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public float getCollisionBorderSize() {
        return super.getCollisionBorderSize();
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
