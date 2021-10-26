package love.marblegate.risinguppercut.entity.watcher;

import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.misc.LootUtil;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class RocketPunchProcessWatcher extends Entity {
    static final DataParameter<Integer> TIMER = EntityDataManager.createKey(RocketPunchProcessWatcher.class, DataSerializers.VARINT);
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
    PlayerEntity source;
    boolean stopTracking = false;


    public RocketPunchProcessWatcher(EntityType<? extends RocketPunchProcessWatcher> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public RocketPunchProcessWatcher(World worldIn, BlockPos pos, int effectiveChargeTime, double knockbackSpeedIndex, double speedIndex, float damagePerEffectiveCharge, double dx, double dz, boolean ignoreArmor, boolean healing, boolean isFireDamage, PlayerEntity source, int shouldLoot) {
        super(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), worldIn);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        dataManager.set(TIMER, effectiveChargeTime);
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
        if (!world.isRemote()) {
            int temp = dataManager.get(TIMER);

            //Deal with rocket punch is valid
            if (temp > 0 && !stopTracking && source != null) {
                //Slightly enlarge player's hitbox
                AxisAlignedBB collideBox = source.getBoundingBox().grow(0.5f, 0, 0.5f);

                //Collision Detection
                List<LivingEntity> checks = world.getEntitiesWithinAABB(LivingEntity.class, collideBox);
                checks.remove(source);

                //If any mob is detected
                if (!checks.isEmpty()) {
                    // spawn an watchEntity to simulate rocket punch effect
                    RocketPunchImpactWatcher watchEntity = new RocketPunchImpactWatcher(world, source.getPosition(), temp, effectiveChargeTime,
                            knockbackSpeedIndex, damagePerEffectiveCharge, dx, dz,
                            ignoreArmor, healing, isFireDamage, source);
                    for (LivingEntity target : checks) {
                        // Deal damage
                        DamageSource damageSource = new RocketPunchDamageSource(source);
                        if (shouldLoot > 0) {
                            LootUtil.dropLoot(target, DamageSource.causePlayerDamage(source), true, source);
                        }
                        if (isFireDamage) {
                            damageSource.setFireDamage();
                            target.setFire(3);
                            target.attackEntityFrom(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        } else if (ignoreArmor) {
                            damageSource.setDamageBypassesArmor();
                            target.attackEntityFrom(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        } else if (healing) {
                            target.heal(damagePerEffectiveCharge * effectiveChargeTime);
                        } else {
                            target.attackEntityFrom(damageSource, damagePerEffectiveCharge * effectiveChargeTime);
                        }

                        if (target.isAlive()) {
                            watchEntity.watch(target);
                        }
                    }
                    source.world.addEntity(watchEntity);

                    // Player stop moving and clear pocket punch status
                    source.setMotion(0, 0, 0);
                    source.markPositionDirty();
                    source.velocityChanged = true;
                    stopTracking = true;
                }

                // If rocket punch is active and player hit a wall
                // stop player and clear rocket punch status
                if (source.collidedHorizontally && !stopTracking) {
                    source.setMotion(0, 0, 0);
                    source.markPositionDirty();
                    source.velocityChanged = true;
                    stopTracking = true;
                }

                // Deal with player rocket punch movement
                if (!stopTracking) {
                    // lock moving direction
                    source.setMotion(dx * speedIndex, 0.1, dz * speedIndex);
                    source.markPositionDirty();
                    source.velocityChanged = true;
                    dataManager.set(TIMER, temp - 1);
                }
            }

            if (stopTracking || source == null || temp == 0) {
                remove();
            }
        }
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
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
