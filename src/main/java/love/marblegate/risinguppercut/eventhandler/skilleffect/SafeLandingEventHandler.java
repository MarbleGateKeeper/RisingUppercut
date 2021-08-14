package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.network.Networking;
import love.marblegate.risinguppercut.network.RemoveEffectSyncToClientPacket;
import love.marblegate.risinguppercut.registry.EffectRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber()
public class SafeLandingEventHandler {

    @SubscribeEvent
    public static void handle(LivingFallEvent event) {
        if (!event.getEntityLiving().world.isRemote && !event.isCanceled()) {
            if (event.getEntityLiving() instanceof PlayerEntity) {
                if (((PlayerEntity) event.getEntityLiving()).isPotionActive(EffectRegistry.SAFE_LANDING.get())) {
                    event.setCanceled(true);
                    if (Configuration.SafeLandingConfig.DISPOSABLE_EFFECT.get()) {
                        ((PlayerEntity) event.getEntityLiving()).removeActivePotionEffect(EffectRegistry.SAFE_LANDING.get());
                        //Sync to client
                        Networking.INSTANCE.send(
                                PacketDistributor.PLAYER.with(
                                        () -> (ServerPlayerEntity) event.getEntityLiving()
                                ),
                                new RemoveEffectSyncToClientPacket(EffectRegistry.SAFE_LANDING.get()));
                    }
                }
            }
        }
    }
}
