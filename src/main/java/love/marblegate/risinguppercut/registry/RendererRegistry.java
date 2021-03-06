package love.marblegate.risinguppercut.registry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "rising_uppercut",bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererRegistry {
    @SubscribeEvent
    public static void onClientSetUpEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ROCKET_PUNCH_PROCESS_WATCHER.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.RISING_UPPERCUT_WATCHER.get(), NoopRenderer::new);
    }
}
