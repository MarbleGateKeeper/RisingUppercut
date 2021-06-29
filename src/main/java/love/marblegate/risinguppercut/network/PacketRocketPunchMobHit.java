package love.marblegate.risinguppercut.network;

import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.IRocketPunchMobHitRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.RocketPunchMobHitRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

//这个包还没写完，是用来发包同步被打击的生物状态的
public class PacketRocketPunchMobHit {
    private final String attackerName;
    private final int strength;
    private final double dX;
    private final double dZ;

    public PacketRocketPunchMobHit(PacketBuffer buffer) {
        this.attackerName = buffer.readString();
        this.dX = buffer.readDouble();
        this.dZ = buffer.readDouble();
        this.strength = buffer.readInt();
    }

    public PacketRocketPunchMobHit(String attackerName, int strength, double dX, double dZ) {
        this.attackerName = attackerName;
        this.dX = dX;
        this.dZ = dZ;
        this.strength = strength;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.attackerName);
        buf.writeDouble(this.dX);
        buf.writeDouble(this.dZ);
        buf.writeInt(this.strength);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                PlayerEntity player = Minecraft.getInstance().player;
                LazyOptional<IRocketPunchMobHitRecord> rkp_cap = player.getCapability(RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD);
                rkp_cap.ifPresent(
                        cap-> {
                            cap.setAttackerName(attackerName);
                            cap.setDirection(dX,dZ);
                            cap.setStrength(strength);
                        }
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
