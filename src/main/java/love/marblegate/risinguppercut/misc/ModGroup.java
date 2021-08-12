package love.marblegate.risinguppercut.misc;

import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroup extends ItemGroup {
    public static final ModGroup INSTANCE = new ModGroup();

    ModGroup() {
        super("rising_uppercut.all");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.GAUNTLET.get());
    }
}
