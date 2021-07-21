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
            compoundNBT.putFloat("rkp_damage",instance.getDamage());
            compoundNBT.putDouble("rkp_dx",instance.getDirectionX());
            compoundNBT.putDouble("rkp_dz",instance.getDirectionZ());
            compoundNBT.putDouble("rkp_speedIndex",instance.getSpeedIndex());
            compoundNBT.putDouble("rkp_knockbackIndex",instance.getKnockbackSpeedIndex());
            compoundNBT.putBoolean("rkp_ignoreArmor",instance.ignoreArmor());
            compoundNBT.putBoolean("rkp_healing",instance.healing());
            compoundNBT.putBoolean("rkp_isFireDamage",instance.isFireDamage());
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<IRocketPunchPlayerSkillRecord> capability, IRocketPunchPlayerSkillRecord instance, Direction side, INBT nbt) {
            int rkp_timer = ((CompoundNBT)nbt).getInt("rkp_timer");
            float rkp_damage = ((CompoundNBT)nbt).getFloat("rkp_damage");
            double rkp_dx = ((CompoundNBT)nbt).getDouble("rkp_dx");
            double rkp_dz = ((CompoundNBT)nbt).getDouble("rkp_dz");
            double rkp_speedIndex = ((CompoundNBT)nbt).getDouble("rkp_speedIndex");
            double rkp_knockbackIndex = ((CompoundNBT)nbt).getDouble("rkp_knockbackIndex");
            boolean rkp_ignoreArmor = ((CompoundNBT)nbt).getBoolean("rkp_ignoreArmor");
            boolean rkp_healing = ((CompoundNBT)nbt).getBoolean("rkp_healing");
            boolean rkp_isFireDamage = ((CompoundNBT)nbt).getBoolean("rkp_isFireDamage");
            instance.setTimer(rkp_timer);
            instance.setDamage(rkp_damage);
            instance.setDirection(rkp_dx,rkp_dz);
            instance.setSpeedIndex(rkp_speedIndex);
            instance.setKnockbackIndex(rkp_knockbackIndex);
            instance.setIsFireDamage(rkp_isFireDamage);
            instance.setHealing(rkp_healing);
            instance.setIgnoreArmor(rkp_ignoreArmor);
        }
    }
}
