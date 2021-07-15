package love.marblegate.risinguppercut.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


public class RocketPunchHitEntity extends Entity {
    private static final DataParameter<Integer> STRENGTH = EntityDataManager.createKey(RocketPunchHitEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIMER = EntityDataManager.createKey(RocketPunchHitEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DIRECTION_X = EntityDataManager.createKey(RocketPunchHitEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DIRECTION_Z = EntityDataManager.createKey(RocketPunchHitEntity.class, DataSerializers.FLOAT);

    public RocketPunchHitEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {
        dataManager.register(STRENGTH,0);
        dataManager.register(TIMER,0);
        dataManager.register(DIRECTION_X,0F);
        dataManager.register(DIRECTION_Z,0F);
    }

    @Override
    public void tick() {

        super.tick();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        dataManager.set(STRENGTH,compound.getInt("strength"));
        dataManager.set(TIMER,compound.getInt("timer"));
        dataManager.set(DIRECTION_X,compound.getFloat("dx"));
        dataManager.set(DIRECTION_Z,compound.getFloat("dy"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("strength",dataManager.get(STRENGTH));
        compound.putInt("timer",dataManager.get(TIMER));
        compound.putFloat("dx",dataManager.get(DIRECTION_X));
        compound.putFloat("dy",dataManager.get(DIRECTION_Z));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public boolean shouldRiderSit() {
        return false;
    }
}
