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
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
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
    List<LivingEntity> watchedEntities ;

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
            watchedEntities.add(livingEntity);
        }
    }

    public void removeFromWatchList(LivingEntity livingEntity){
        if(livingEntity != null){
            watchedEntities.remove(livingEntity);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote()){
            int temp = dataManager.get(TIMER);
            if(watchedEntities!=null&&source!=null){
                if(!watchedEntities.isEmpty()){
                    List<LivingEntity> entitiesRemoveFromWatchList = new ArrayList<>();
                    for(LivingEntity entity:watchedEntities){
                        if(entity.collidedHorizontally){
                            entity.attackEntityFrom(new RocketPunchOnWallDamageSource(source),strength*0.5f);
                            entitiesRemoveFromWatchList.add(entity);
                        } else {
                            entity.setMotion(dx * SPEED_INDEX,0,dz * SPEED_INDEX);
                            entity.markPositionDirty();
                            entity.velocityChanged = true;
                        }
                    }
                    for(LivingEntity remove:entitiesRemoveFromWatchList){
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
}
