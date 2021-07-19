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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;


public class RocketPunchWatcher extends Entity {
    static final double SPEED_INDEX = 2;
    static final DataParameter<Integer> TIMER = EntityDataManager.createKey(RocketPunchWatcher.class, DataSerializers.VARINT);
    double dx;
    double dz;
    int strength;
    PlayerEntity source;
    List<YUnchangedLivingEntity> watchedEntities ;

    public RocketPunchWatcher(World worldIn, BlockPos pos, double dx, double dz, int strength, int timer, PlayerEntity source) {
        super(EntityRegistry.rocket_punch_watcher.get(), worldIn);
        setPosition(pos.getX(),pos.getY(),pos.getZ());
        this.dx = dx;
        this.dz = dz;
        this.strength = strength;
        dataManager.set(TIMER,timer);
        this.source = source;
        watchedEntities = new ArrayList<>();
    }

    public RocketPunchWatcher(EntityType<? extends RocketPunchWatcher> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
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
                            entity.livingEntity.attackEntityFrom(new RocketPunchOnWallDamageSource(source),strength*0.5f);
                            entitiesRemoveFromWatchList.add(entity);
                        } else {
                            entity.setMotion(dx * SPEED_INDEX,dz * SPEED_INDEX);
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
        watchedEntities = new ArrayList<>();
        source = null;
        strength = compound.getInt("strength");
        dataManager.set(TIMER,compound.getInt("timer"));
        dx = compound.getDouble("dx");
        dz = compound.getDouble("dz");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("strength",strength);
        compound.putInt("timer",dataManager.get(TIMER));
        compound.putDouble("dx",dx);
        compound.putDouble("dz",dz);
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
