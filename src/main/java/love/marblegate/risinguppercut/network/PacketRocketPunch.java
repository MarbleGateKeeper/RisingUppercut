package love.marblegate.risinguppercut.network;

import love.marblegate.risinguppercut.capability.rocketpunch.IRocketPunchIndicator;
import love.marblegate.risinguppercut.capability.rocketpunch.RocketPunchIndicator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRocketPunch {
    private final int indicator;

    public PacketRocketPunch(PacketBuffer buffer) {
        this.indicator = buffer.readInt();
    }

    public PacketRocketPunch(int indicator) {
        this.indicator = indicator;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.indicator);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                PlayerEntity player = Minecraft.getInstance().player;
                LazyOptional<IRocketPunchIndicator> rkp_cap = player.getCapability(RocketPunchIndicator.ROCKET_PUNCH_INDICATOR);
                rkp_cap.ifPresent(
                        cap-> {
                            cap.set(indicator);
                        }
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
