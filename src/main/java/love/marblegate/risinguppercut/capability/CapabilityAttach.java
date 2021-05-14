package love.marblegate.risinguppercut.capability;

import love.marblegate.risinguppercut.capability.rocketpunch.RocketPunchIndicatorProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class CapabilityAttach {
    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation("rising_uppercut", "rocket_punch_indicator"), new RocketPunchIndicatorProvider());
        }
    }
}
