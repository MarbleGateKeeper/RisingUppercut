package love.marblegate.risinguppercut.registry;

import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, "rising_uppercut");
    public static final RegistryObject<Item> gauntlet = ITEM.register("gauntlet", () -> new Gauntlet());

}
