package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.item.BaseComponent;
import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, "rising_uppercut");
    public static final RegistryObject<Item> GAUNTLET = ITEM.register("gauntlet", Gauntlet::new);
    public static final RegistryObject<Item> GAUNTLET_SURFACE = ITEM.register("gauntlet_surface", BaseComponent::new);
    public static final RegistryObject<Item> GAUNTLET_KINETIC_CORE = ITEM.register("gauntlet_kinetic_core", BaseComponent::new);
    public static final RegistryObject<Item> GAUNTLET_COORDINATE_DRIVE = ITEM.register("gauntlet_coordinate_drive", BaseComponent::new);
    public static final RegistryObject<Item> GAUNTLET_COOLER = ITEM.register("gauntlet_cooler", BaseComponent::new);
}
