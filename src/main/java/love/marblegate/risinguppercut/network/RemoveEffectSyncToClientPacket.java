package love.marblegate.risinguppercut.network;

import love.marblegate.risinguppercut.misc.proxy.ClientProxy;
import love.marblegate.risinguppercut.misc.proxy.IProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveEffectSyncToClientPacket {
    private final Effect effect;
    public static IProxy proxy = new IProxy() {};

    public RemoveEffectSyncToClientPacket(Effect effect) {
        this.effect = effect;
    }

    public RemoveEffectSyncToClientPacket(PacketBuffer buffer) {
        effect = buffer.readRegistryIdSafe(Effect.class);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeRegistryId(effect);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()-> () -> {
            ctx.get().enqueueWork(() -> {
                proxy = new ClientProxy();
                proxy.removeEffect(effect);
            });
            ctx.get().setPacketHandled(true);
        });
    }
}