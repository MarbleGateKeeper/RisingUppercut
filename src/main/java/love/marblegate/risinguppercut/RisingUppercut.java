package love.marblegate.risinguppercut;

import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.misc.CustomEnchantmentType;
import love.marblegate.risinguppercut.registry.MobEffectRegistry;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import love.marblegate.risinguppercut.registry.EntityRegistry;
import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("rising_uppercut")
public class RisingUppercut {

    public RisingUppercut() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.MOD_CONFIG);

        ItemRegistry.ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityRegistry.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        MobEffectRegistry.EFFECT.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentRegistry.ENCHANTMENT.register(FMLJavaModLoadingContext.get().getModEventBus());

        CustomEnchantmentType.addToItemGroup();
    }
}
