package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.item.BaseComponent;
import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, "rising_uppercut");
    public static final RegistryObject<Item> gauntlet = ITEM.register("gauntlet", Gauntlet::new);
    public static final RegistryObject<Item> gauntlet_surface = ITEM.register("gauntlet_surface", BaseComponent::new);
    public static final RegistryObject<Item> gauntlet_kinetic_core = ITEM.register("gauntlet_kinetic_core", BaseComponent::new);
    public static final RegistryObject<Item> gauntlet_coordinate_drive = ITEM.register("gauntlet_coordinate_drive", BaseComponent::new);
    public static final RegistryObject<Item> gauntlet_cooler = ITEM.register("gauntlet_cooler", BaseComponent::new);
}
