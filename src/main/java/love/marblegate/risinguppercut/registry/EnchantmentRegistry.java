package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "rising_uppercut");
    public static final RegistryObject<Enchantment> marblegate_aoe_attack = ENCHANTMENT.register("marblegate_aoe_attack", MGAOEAttack::new);
    public static final RegistryObject<Enchantment> dragonbite = ENCHANTMENT.register("dragonbite", Dragonbite::new);
    public static final RegistryObject<Enchantment> flameburst = ENCHANTMENT.register("flameburst", FlameBurst::new);
    public static final RegistryObject<Enchantment> guardian_angel = ENCHANTMENT.register("guardian_angel", GuardianAngel::new);
    public static final RegistryObject<Enchantment> marblegate_kinetic_optimization = ENCHANTMENT.register("marblegate_kinetic_optimization", MGKineticOptimization::new);
    public static final RegistryObject<Enchantment> kadokawa_kinetic_optimization = ENCHANTMENT.register("kadokawa_kinetic_optimization", KKineticOptimization::new);
    public static final RegistryObject<Enchantment> marblegate_calculation_assist = ENCHANTMENT.register("marblegate_calculation_assist", MGCalculationAssist::new);
    public static final RegistryObject<Enchantment> kadokawa_calculation_assist = ENCHANTMENT.register("kadokawa_calculation_assist", KCalculationAssist::new);
    public static final RegistryObject<Enchantment> marblegate_cooling_assist = ENCHANTMENT.register("marblegate_cooling_assist", MGCoolingAssist::new);
    public static final RegistryObject<Enchantment> kadokawa_cooling_assist = ENCHANTMENT.register("kadokawa_cooling_assist", KCoolingAssist::new);
}
