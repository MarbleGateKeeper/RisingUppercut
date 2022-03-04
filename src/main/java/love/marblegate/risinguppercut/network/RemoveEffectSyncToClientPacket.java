package love.marblegate.risinguppercut.network;

import love.marblegate.risinguppercut.misc.proxy.ClientProxy;
import love.marblegate.risinguppercut.misc.proxy.IProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveEffectSyncToClientPacket {
    private final MobEffect mobEffect;
    public static IProxy proxy = new IProxy() {
    };

    public RemoveEffectSyncToClientPacket(MobEffect mobEffect) {
        this.mobEffect = mobEffect;
    }

    public RemoveEffectSyncToClientPacket(FriendlyByteBuf friendlyByteBuf) {
        mobEffect = friendlyByteBuf.readRegistryIdSafe(MobEffect.class);
    }

    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeRegistryId(mobEffect);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ctx.get().enqueueWork(() -> {
                proxy = new ClientProxy();
                proxy.removeEffect(mobEffect);
            });
            ctx.get().setPacketHandled(true);
        });
    }
}
