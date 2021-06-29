package love.marblegate.risinguppercut.network;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRocketPunchStatus {
    private final int timer;
    private final int strength;
    private final double dX;
    private final double dZ;

    public PacketRocketPunchStatus(PacketBuffer buffer) {
        this.timer = buffer.readInt();
        this.dX = buffer.readDouble();
        this.dZ = buffer.readDouble();
        this.strength = buffer.readInt();
    }

    public PacketRocketPunchStatus(int timer, int strength, double dX, double dZ) {
        this.timer = timer;
        this.dX = dX;
        this.dZ = dZ;
        this.strength = strength;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.timer);
        buf.writeDouble(this.dX);
        buf.writeDouble(this.dZ);
        buf.writeInt(this.strength);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                PlayerEntity player = Minecraft.getInstance().player;
                LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = player.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);
                rkp_cap.ifPresent(
                        cap-> {
                            cap.setTimer(timer);
                            cap.setDirection(dX,dZ);
                            cap.setStrength(strength);
                        }
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
