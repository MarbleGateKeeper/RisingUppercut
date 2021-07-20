package love.marblegate.risinguppercut.util;

import love.marblegate.risinguppercut.item.Gauntlet;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.*;

import static net.minecraft.enchantment.EnchantmentType.BREAKABLE;
import static net.minecraft.enchantment.EnchantmentType.WEARABLE;

public class CustomEnchantmentType {
    public static final EnchantmentType GAUNTLET = EnchantmentType.create("rising_uppercut:GAUNTLET", Item-> Item instanceof Gauntlet);

    public static void addToItemGroup() {
        EnchantmentType[] GAUNTLET_TYPES = new EnchantmentType[ModGroup.INSTANCE.getRelevantEnchantmentTypes().length + 1];
        for (int i = 0; i < ModGroup.INSTANCE.getRelevantEnchantmentTypes().length; i++) {
            GAUNTLET_TYPES[i] = ModGroup.INSTANCE.getRelevantEnchantmentTypes()[i];
        }

        GAUNTLET_TYPES[GAUNTLET_TYPES.length - 1] = GAUNTLET;
        ModGroup.INSTANCE.setRelevantEnchantmentTypes(GAUNTLET_TYPES);

    }
}
