package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "rising_uppercut");
    public static final RegistryObject<Enchantment> MARBLEGATE_LOOTING = ENCHANTMENT.register("marblegate_looting", MGAOEAttack::new);
    public static final RegistryObject<Enchantment> MARBLEGATE_AOE_ATTACK = ENCHANTMENT.register("marblegate_aoe_attack", MGAOEAttack::new);
    public static final RegistryObject<Enchantment> DRAGONBITE = ENCHANTMENT.register("dragonbite", Dragonbite::new);
    public static final RegistryObject<Enchantment> FLAMEBURST = ENCHANTMENT.register("flameburst", FlameBurst::new);
    public static final RegistryObject<Enchantment> GUARDIAN_ANGEL = ENCHANTMENT.register("guardian_angel", GuardianAngel::new);
    public static final RegistryObject<Enchantment> MARBLEGATE_KINETIC_OPTIMIZATION = ENCHANTMENT.register("marblegate_kinetic_optimization", MGKineticOptimization::new);
    public static final RegistryObject<Enchantment> KADOKAWA_KINETIC_OPTIMIZATION = ENCHANTMENT.register("kadokawa_kinetic_optimization", KKineticOptimization::new);
    public static final RegistryObject<Enchantment> MARBLEGATE_CALCULATION_ASSIST = ENCHANTMENT.register("marblegate_calculation_assist", MGCalculationAssist::new);
    public static final RegistryObject<Enchantment> KADOKAWA_CALCULATION_ASSIST = ENCHANTMENT.register("kadokawa_calculation_assist", KCalculationAssist::new);
    public static final RegistryObject<Enchantment> MARBLEGATE_COOLING_ASSIST = ENCHANTMENT.register("marblegate_cooling_assist", MGCoolingAssist::new);
    public static final RegistryObject<Enchantment> KADOKAWA_COOLING_ASSIST = ENCHANTMENT.register("kadokawa_cooling_assist", KCoolingAssist::new);
}
