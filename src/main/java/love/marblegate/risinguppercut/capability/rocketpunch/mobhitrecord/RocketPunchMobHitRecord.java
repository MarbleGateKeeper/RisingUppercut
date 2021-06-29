package love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class RocketPunchMobHitRecord {
    @CapabilityInject(IRocketPunchMobHitRecord.class)
    public static Capability<IRocketPunchMobHitRecord> ROCKET_PUNCH_MOB_HIT_RECORD = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IRocketPunchMobHitRecord.class, new Storage(), RocketPunchMobHitRecordStandardImpl::new);
    }

    public static class Storage implements Capability.IStorage<IRocketPunchMobHitRecord> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IRocketPunchMobHitRecord> capability, IRocketPunchMobHitRecord instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("rkp_hit_strength",instance.getStrength());
            compoundNBT.putString("rkp_hit_attacker_name",instance.getAttackerName());
            compoundNBT.putDouble("rkp_hit_dx",instance.getDirectionX());
            compoundNBT.putDouble("rkp_hit_dz",instance.getDirectionZ());
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IRocketPunchMobHitRecord> capability, IRocketPunchMobHitRecord instance, Direction side, INBT nbt) {
            int rkp_hit_strength = ((CompoundNBT)nbt).getInt("rkp_hit_strength");
            String rkp_hit_attacker_name = ((CompoundNBT)nbt).getString("rkp_hit_attacker_name");
            double rkp_hit_dx =  ((CompoundNBT)nbt).getDouble("rkp_hit_dx");
            double rkp_hit_dz =  ((CompoundNBT)nbt).getDouble("rkp_hit_dz");
            instance.setStrength(rkp_hit_strength);
            instance.setAttackerName(rkp_hit_attacker_name);
            instance.setDirection(rkp_hit_dx,rkp_hit_dz);
        }
    }
}
