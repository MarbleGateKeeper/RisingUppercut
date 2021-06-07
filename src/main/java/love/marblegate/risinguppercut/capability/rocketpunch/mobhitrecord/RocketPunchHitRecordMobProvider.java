package love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RocketPunchHitRecordMobProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IRocketPunchMobHitRecord imp = new RocketPunchMobHitRecordStandardImpl();
    private final LazyOptional<IRocketPunchMobHitRecord> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD){
            return impOptional.cast();
        }
        else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD.writeNBT(imp, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD != null) {
            RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD.readNBT(imp, null, nbt);
        }
    }
}
