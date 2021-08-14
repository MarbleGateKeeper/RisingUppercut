package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "rising_uppercut");
    public static final RegistryObject<Enchantment> MARBLEGATE_LOOTING = ENCHANTMENT.register("marblegate_looting", AOEAttack::new);
    public static final RegistryObject<Enchantment> AOE_ATTACK = ENCHANTMENT.register("aoe_attack", AOEAttack::new);
    public static final RegistryObject<Enchantment> DRAGONBITE = ENCHANTMENT.register("dragonbite", Dragonbite::new);
    public static final RegistryObject<Enchantment> FLAMEBURST = ENCHANTMENT.register("flameburst", FlameBurst::new);
    public static final RegistryObject<Enchantment> GUARDIAN_ANGEL = ENCHANTMENT.register("guardian_angel", GuardianAngel::new);
    public static final RegistryObject<Enchantment> SOFTFALLING = ENCHANTMENT.register("softfalling", SoftFalling::new);
    public static final RegistryObject<Enchantment> MARBLEGATE_KINETIC_OPTIMIZATION = ENCHANTMENT.register("marblegate_kinetic_optimization", MGKineticOptimization::new);
    public static final RegistryObject<Enchantment> KADOKAWA_KINETIC_OPTIMIZATION = ENCHANTMENT.register("kadokawa_kinetic_optimization", KKineticOptimization::new);
    public static final RegistryObject<Enchantment> RISING_UPPERCUT_CALCULATION_ASSIST = ENCHANTMENT.register("rising_uppercut_calculation_assist", RisingUppercutCalculationAssist::new);
    public static final RegistryObject<Enchantment> ROCKET_PUNCH_CALCULATION_ASSIST = ENCHANTMENT.register("rocket_punch_calculation_assist", RocketPunchCalculationAssist::new);
    public static final RegistryObject<Enchantment> RISING_UPPERCUT_COOLING_ASSIST = ENCHANTMENT.register("rising_uppercut_cooling_assist", RisingUppercutCoolingAssist::new);
    public static final RegistryObject<Enchantment> ROCKET_PUNCH_COOLING_ASSIST = ENCHANTMENT.register("rocket_punch_cooling_assist", RocketPunchCoolingAssist::new);
}
