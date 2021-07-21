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
    final int timer;
    final float damage;
    final double index1;
    final double index2;
    final double dX;
    final double dZ;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;

    public PacketRocketPunchStatus(PacketBuffer buffer) {
        timer = buffer.readInt();
        damage = buffer.readFloat();
        index1 = buffer.readDouble();
        index2 = buffer.readDouble();
        dX = buffer.readDouble();
        dZ = buffer.readDouble();
        ignoreArmor = buffer.readBoolean();
        healing = buffer.readBoolean();
        isFireDamage = buffer.readBoolean();
    }

    public PacketRocketPunchStatus(int timer, float damage, double index1, double index2, double dX, double dZ, boolean ignoreArmor, boolean healing, boolean isFireDamage) {
        this.timer = timer;
        this.damage = damage;
        this.index1 = index1;
        this.index2 = index2;
        this.dX = dX;
        this.dZ = dZ;
        this.ignoreArmor = ignoreArmor;
        this.healing = healing;
        this.isFireDamage = isFireDamage;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(timer);
        buf.writeFloat(damage);
        buf.writeDouble(index1);
        buf.writeDouble(index2);
        buf.writeDouble(dX);
        buf.writeDouble(dZ);
        buf.writeBoolean(ignoreArmor);
        buf.writeBoolean(healing);
        buf.writeBoolean(isFireDamage);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                PlayerEntity player = Minecraft.getInstance().player;
                LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = player.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);
                rkp_cap.ifPresent(
                        cap-> {
                            cap.setTimer(timer);
                            cap.setDamage(damage);
                            cap.setSpeedIndex(index1);
                            cap.setKnockbackIndex(index2);
                            cap.setDirection(dX,dZ);
                            cap.setIgnoreArmor(ignoreArmor);
                            cap.setHealing(healing);
                            cap.setIsFireDamage(isFireDamage);
                        }
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
