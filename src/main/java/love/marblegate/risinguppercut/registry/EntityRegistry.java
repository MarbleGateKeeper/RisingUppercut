package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.entity.rocketpunchhit.RocketPunchHitEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "rising_uppercut");
    public static final RegistryObject<EntityType<RocketPunchHitEntity>> rocket_punch_hit_effect = ENTITY_TYPES.register("rocket_punch_hit_effect", () -> EntityType.Builder.<RocketPunchHitEntity>create(RocketPunchHitEntity::new, EntityClassification.MISC).immuneToFire().size(0.01F, 0.01F).build("rocket_punch_hit_effect"));
}
