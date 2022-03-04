package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.network.Networking;
import love.marblegate.risinguppercut.network.RemoveEffectSyncToClientPacket;
import love.marblegate.risinguppercut.registry.MobEffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber()
public class SafeLandingEventHandler {

    @SubscribeEvent
    public static void handle(LivingFallEvent event) {
        if (!event.getEntityLiving().level.isClientSide && !event.isCanceled()) {
            if (event.getEntityLiving() instanceof Player) {
                if (((Player) event.getEntityLiving()).hasEffect(MobEffectRegistry.SAFE_LANDING.get())) {
                    event.setCanceled(true);
                    if (Configuration.SafeLandingConfig.DISPOSABLE_EFFECT.get()) {
                        ((Player) event.getEntityLiving()).removeEffectNoUpdate(MobEffectRegistry.SAFE_LANDING.get());
                        //Sync to client
                        Networking.INSTANCE.send(
                                PacketDistributor.PLAYER.with(
                                        () -> (ServerPlayer) event.getEntityLiving()
                                ),
                                new RemoveEffectSyncToClientPacket(MobEffectRegistry.SAFE_LANDING.get()));
                    }
                }
            }
        }
    }
}
