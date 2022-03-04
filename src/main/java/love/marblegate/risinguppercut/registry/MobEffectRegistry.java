package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.effect.SafeLanding;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "rising_uppercut");
    public static final RegistryObject<MobEffect> SAFE_LANDING = EFFECT.register("safe_landing", SafeLanding::new);
}
