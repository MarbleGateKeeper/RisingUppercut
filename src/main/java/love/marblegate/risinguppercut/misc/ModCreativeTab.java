package love.marblegate.risinguppercut.misc;

import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab extends CreativeModeTab {
    public static final ModCreativeTab INSTANCE = new ModCreativeTab();

    ModCreativeTab() {
        super("rising_uppercut.all");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.GAUNTLET.get());
    }
}
