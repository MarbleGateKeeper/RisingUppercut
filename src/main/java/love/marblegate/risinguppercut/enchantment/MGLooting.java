package love.marblegate.risinguppercut.enchantment;

import love.marblegate.risinguppercut.misc.CustomEnchantmentType;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class MGLooting extends Enchantment {
    public MGLooting() {
        super(Rarity.VERY_RARE, CustomEnchantmentType.GAUNTLET, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public boolean canApplyTogether(Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_)
                && p_77326_1_ != EnchantmentRegistry.GUARDIAN_ANGEL.get();
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 25 + p_77321_1_ * 5;
    }

    @Override
    public int getMaxEnchantability(int p_223551_1_) {
        return getMinEnchantability(p_223551_1_) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}
