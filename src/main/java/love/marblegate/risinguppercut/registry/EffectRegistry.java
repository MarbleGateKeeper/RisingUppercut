package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.effect.RocketPunchHitEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, "rising_uppercut");
    public static final RegistryObject<Effect> rocket_punch_hit = EFFECT.register("rocket_punch_hit", () -> new RocketPunchHitEffect());


}
