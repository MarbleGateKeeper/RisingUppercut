package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistry {
    @SubscribeEvent
    public static void onSetUpEvent(FMLCommonSetupEvent event) {
        RocketPunchPlayerSkillRecord.register();
    }
}
