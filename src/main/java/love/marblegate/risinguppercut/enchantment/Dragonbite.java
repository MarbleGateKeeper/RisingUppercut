package love.marblegate.risinguppercut.enchantment;

import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import love.marblegate.risinguppercut.misc.CustomEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class Dragonbite extends Enchantment{
    public Dragonbite() {
        super(Rarity.VERY_RARE , CustomEnchantmentType.GAUNTLET, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public boolean canApplyTogether(Enchantment p_77326_1_) {
        return super.canApplyTogether(p_77326_1_)
                && p_77326_1_ != EnchantmentRegistry.FLAMEBURST.get()
                && p_77326_1_ != EnchantmentRegistry.GUARDIAN_ANGEL.get();
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
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
