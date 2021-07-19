package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchWatcher;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "rising_uppercut");
    public static final RegistryObject<EntityType<RocketPunchWatcher>> rocket_punch_watcher = ENTITY_TYPES.register("rocket_punch_watcher", () -> EntityType.Builder.<RocketPunchWatcher>create(RocketPunchWatcher::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rocket_punch_watcher"));
    public static final RegistryObject<EntityType<RisingUppercutWatcher>> rising_uppercut_watcher = ENTITY_TYPES.register("rising_uppercut_watcher", () -> EntityType.Builder.<RisingUppercutWatcher>create(RisingUppercutWatcher::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rising_uppercut_watcher"));

}
