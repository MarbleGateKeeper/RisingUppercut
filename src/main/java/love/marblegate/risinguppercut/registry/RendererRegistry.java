package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.entity.BlankRender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererRegistry {
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ROCKET_PUNCH_IMPACT_WATCHER.get(), BlankRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.RISING_UPPERCUT_WATCHER.get(), BlankRender::new);
    }
}
