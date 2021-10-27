package love.marblegate.risinguppercut.misc;

import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CustomEnchantmentType {
    public static final EnchantmentCategory GAUNTLET = EnchantmentCategory.create("rising_uppercut:GAUNTLET", item -> item instanceof Gauntlet);

    public static void addToItemGroup() {
        EnchantmentCategory[] GAUNTLET_TYPES = new EnchantmentCategory[ModCreativeTab.INSTANCE.getEnchantmentCategories().length + 3];
        for (int i = 0; i < ModCreativeTab.INSTANCE.getEnchantmentCategories().length; i++) {
            GAUNTLET_TYPES[i] = ModCreativeTab.INSTANCE.getEnchantmentCategories()[i];
        }

        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 3] = GAUNTLET;
        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 2] = EnchantmentCategory.BREAKABLE;
        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 1] = EnchantmentCategory.VANISHABLE;
        ModCreativeTab.INSTANCE.setEnchantmentCategories(GAUNTLET_TYPES);

    }
}
