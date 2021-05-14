package love.marblegate.risinguppercut.capability.rocketpunch;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RocketPunchIndicatorProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IRocketPunchIndicator imp = new RocketPunchIndicatorStandardImpl();
    private final LazyOptional<IRocketPunchIndicator> impOptional = LazyOptional.of(() -> imp);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == RocketPunchIndicator.ROCKET_PUNCH_INDICATOR){
            return impOptional.cast();
        }
        else return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (RocketPunchIndicator.ROCKET_PUNCH_INDICATOR == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) RocketPunchIndicator.ROCKET_PUNCH_INDICATOR.writeNBT(imp, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (RocketPunchIndicator.ROCKET_PUNCH_INDICATOR != null) {
            RocketPunchIndicator.ROCKET_PUNCH_INDICATOR.readNBT(imp, null, nbt);
        }
    }
}
