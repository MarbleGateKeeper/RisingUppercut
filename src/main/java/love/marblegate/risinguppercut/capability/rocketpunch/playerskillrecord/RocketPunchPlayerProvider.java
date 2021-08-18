package love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RocketPunchPlayerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IRocketPunchPlayerSkillRecord imp = new RocketPunchPlayerSkillRecordStandardImpl();
    private final LazyOptional<IRocketPunchPlayerSkillRecord> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD) {
            return impOptional.cast();
        } else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD.writeNBT(imp, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD != null) {
            RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD.readNBT(imp, null, nbt);
        }
    }
}
