package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.effect.SafeLanding;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, "rising_uppercut");
    public static final RegistryObject<Effect> SAFE_LANDING = EFFECT.register("safe_landing", SafeLanding::new);
}
