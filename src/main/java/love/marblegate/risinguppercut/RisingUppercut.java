package love.marblegate.risinguppercut;

import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("rising_uppercut")
public class RisingUppercut {
    public RisingUppercut(){
        ItemRegistry.ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
