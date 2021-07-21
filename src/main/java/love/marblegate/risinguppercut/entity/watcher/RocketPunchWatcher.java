package love.marblegate.risinguppercut.entity.watcher;

import love.marblegate.risinguppercut.damagesource.RocketPunchOnWallDamageSource;
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


public class RocketPunchWatcher extends Entity {
    static final DataParameter<Integer> TIMER = EntityDataManager.createKey(RocketPunchWatcher.class, DataSerializers.VARINT);
    double knockbackSpeedIndex;
    float damage;
    double dx;
    double dz;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    PlayerEntity source;
    List<YUnchangedLivingEntity> watchedEntities ;


    public RocketPunchWatcher(EntityType<? extends RocketPunchWatcher> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public RocketPunchWatcher(World worldIn, BlockPos pos, int timer, double knockbackSpeedIndex, float damage, double dx, double dz, boolean ignoreArmor, boolean healing, boolean isFireDamage, PlayerEntity source) {
        super(EntityRegistry.ROCKET_PUNCH_WATCHER.get(), worldIn);
        setPosition(pos.getX(),pos.getY(),pos.getZ());
        dataManager.set(TIMER,timer);
        this.knockbackSpeedIndex = knockbackSpeedIndex;
        this.damage = damage;
        this.dx = dx;
        this.dz = dz;
        this.source = source;
        this.isFireDamage = isFireDamage;
        this.healing = healing;
        this.ignoreArmor = ignoreArmor;
        watchedEntities = new ArrayList<>();
    }

    public void watch(LivingEntity livingEntity){
        if(livingEntity != null){
            watchedEntities.add(new YUnchangedLivingEntity(livingEntity));
        }
    }

    public void removeFromWatchList(YUnchangedLivingEntity yUnchangedLivingEntity){
        if(yUnchangedLivingEntity != null){
            watchedEntities.remove(yUnchangedLivingEntity);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote()){
            int temp = dataManager.get(TIMER);
            if(watchedEntities!=null&&source!=null){
                if(!watchedEntities.isEmpty()){
                    List<YUnchangedLivingEntity> entitiesRemoveFromWatchList = new ArrayList<>();
                    for(YUnchangedLivingEntity entity:watchedEntities){
                        if(entity.livingEntity.collidedHorizontally){
                            DamageSource damageSource = new RocketPunchOnWallDamageSource(source);
                            {
                                if(ignoreArmor){
                                    damageSource.setDamageBypassesArmor();
                                    entity.livingEntity.attackEntityFrom(damageSource,damage+1);
                                } else if(isFireDamage){
                                    damageSource.setFireDamage();
                                    entity.livingEntity.attackEntityFrom(damageSource,damage+1);
                                } else if(healing){
                                    entity.livingEntity.heal(damage+1);
                                } else {
                                    entity.livingEntity.attackEntityFrom(damageSource,damage+1);
                                }
                            }
                            entitiesRemoveFromWatchList.add(entity);
                        } else {
                            entity.setMotion(dx * knockbackSpeedIndex,dz * knockbackSpeedIndex);
                        }
                    }
                    for(YUnchangedLivingEntity remove:entitiesRemoveFromWatchList){
                        removeFromWatchList(remove);
                    }
                    if(temp - 1 == 0) {
                        watchedEntities.clear();
                        remove();
                    }
                    else dataManager.set(TIMER,temp-1);
                } else {
                    if(temp - 1 == 0) remove();
                    else dataManager.set(TIMER,temp-1);
                }
            } else {
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
        dataManager.register(TIMER,0);
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

    static class YUnchangedLivingEntity{
        LivingEntity livingEntity;
        double Y;

        public YUnchangedLivingEntity(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
            Y = livingEntity.getPosY();
        }

        void setMotion(double X, double Z){
            livingEntity.setMotion(X,0,Z);
            livingEntity.setPosition(livingEntity.getPosX(),Y,livingEntity.getPosZ());
            livingEntity.markPositionDirty();
            livingEntity.velocityChanged = true;
        }

    }
}
