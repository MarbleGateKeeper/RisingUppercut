package love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class RocketPunchPlayerSkillRecord {
    @CapabilityInject(IRocketPunchPlayerSkillRecord.class)
    public static Capability<IRocketPunchPlayerSkillRecord> ROCKET_PUNCH_SKILL_RECORD = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IRocketPunchPlayerSkillRecord.class, new Storage(), RocketPunchPlayerSkillRecordStandardImpl::new);
    }

    public static class Storage implements Capability.IStorage<IRocketPunchPlayerSkillRecord> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IRocketPunchPlayerSkillRecord> capability, IRocketPunchPlayerSkillRecord instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("rkp_timer",instance.getTimer());
            compoundNBT.putInt("rkp_strength",instance.getStrength());
            compoundNBT.putDouble("rkp_dx",instance.getDirectionX());
            compoundNBT.putDouble("rkp_dz",instance.getDirectionZ());
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IRocketPunchPlayerSkillRecord> capability, IRocketPunchPlayerSkillRecord instance, Direction side, INBT nbt) {
            int rkp_timer = ((CompoundNBT)nbt).getInt("rkp_timer");
            int rkp_strength = ((CompoundNBT)nbt).getInt("rkp_strength");
            double rkp_dx = ((CompoundNBT)nbt).getDouble("rkp_dx");
            double rkp_dz = ((CompoundNBT)nbt).getDouble("rkp_dz");
            instance.setTimer(rkp_timer);
            instance.setStrength(rkp_strength);
            instance.setDirection(rkp_dx,rkp_dz);
        }
    }
}
