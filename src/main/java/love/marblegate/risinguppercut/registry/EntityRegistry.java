package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchImpactWatcher;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchProcessWatcher;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "rising_uppercut");
    public static final RegistryObject<EntityType<RocketPunchProcessWatcher>> ROCKET_PUNCH_PROCESS_WATCHER = ENTITY_TYPES.register("rocket_punch_process_watcher", () -> EntityType.Builder.<RocketPunchProcessWatcher>create(RocketPunchProcessWatcher::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rocket_punch_process_watcher"));
    public static final RegistryObject<EntityType<RocketPunchImpactWatcher>> ROCKET_PUNCH_IMPACT_WATCHER = ENTITY_TYPES.register("rocket_punch_watcher", () -> EntityType.Builder.<RocketPunchImpactWatcher>create(RocketPunchImpactWatcher::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rocket_punch_watcher"));
    public static final RegistryObject<EntityType<RisingUppercutWatcher>> RISING_UPPERCUT_WATCHER = ENTITY_TYPES.register("rising_uppercut_watcher", () -> EntityType.Builder.<RisingUppercutWatcher>create(RisingUppercutWatcher::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rising_uppercut_watcher"));
}
