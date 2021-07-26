package love.marblegate.risinguppercut;

import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import love.marblegate.risinguppercut.registry.EntityRegistry;
import love.marblegate.risinguppercut.registry.ItemRegistry;
import love.marblegate.risinguppercut.util.CustomEnchantmentType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("rising_uppercut")
public class RisingUppercut {
    //TODO
    // - (high priority) Recipe System
    // - Particle Effect
    // - Per Enchantment Texture
    // - (Low priority) More Enchantment
    public RisingUppercut(){
        ItemRegistry.ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityRegistry.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentRegistry.ENCHANTMENT.register(FMLJavaModLoadingContext.get().getModEventBus());

        CustomEnchantmentType.addToItemGroup();
    }
}
