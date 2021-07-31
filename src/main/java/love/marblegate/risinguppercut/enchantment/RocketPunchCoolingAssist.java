package love.marblegate.risinguppercut.enchantment;

import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import love.marblegate.risinguppercut.util.CustomEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class RocketPunchCoolingAssist extends Enchantment{
    public RocketPunchCoolingAssist() {
        super(Rarity.RARE , CustomEnchantmentType.GAUNTLET, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public boolean canApplyTogether(Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_)
                && p_77326_1_ != EnchantmentRegistry.RISING_UPPERCUT_COOLING_ASSIST.get();
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int p_223551_1_) {
        return 60;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}
