package love.marblegate.risinguppercut.misc;

import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.enchantment.EnchantmentType;

import static net.minecraft.enchantment.EnchantmentType.BREAKABLE;

public class CustomEnchantmentType {
    public static final EnchantmentType GAUNTLET = EnchantmentType.create("rising_uppercut:GAUNTLET", Item-> Item instanceof Gauntlet);

    public static void addToItemGroup() {
        EnchantmentType[] GAUNTLET_TYPES = new EnchantmentType[ModGroup.INSTANCE.getRelevantEnchantmentTypes().length + 3];
        for (int i = 0; i < ModGroup.INSTANCE.getRelevantEnchantmentTypes().length; i++) {
            GAUNTLET_TYPES[i] = ModGroup.INSTANCE.getRelevantEnchantmentTypes()[i];
        }

        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 3] = GAUNTLET;
        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 2] = BREAKABLE;
        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 1] = EnchantmentType.VANISHABLE;
        ModGroup.INSTANCE.setRelevantEnchantmentTypes(GAUNTLET_TYPES);

    }
}
