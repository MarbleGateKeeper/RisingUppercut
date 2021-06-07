package love.marblegate.risinguppercut;

import love.marblegate.risinguppercut.registry.EffectRegistry;
import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("rising_uppercut")
public class RisingUppercut {
    public static final Logger LOGGER = LogManager.getLogger();

    public RisingUppercut(){
        ItemRegistry.ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
        EffectRegistry.EFFECT.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
