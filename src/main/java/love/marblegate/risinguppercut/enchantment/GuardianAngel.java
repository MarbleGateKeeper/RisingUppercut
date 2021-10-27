package love.marblegate.risinguppercut.enchantment;

import love.marblegate.risinguppercut.misc.CustomEnchantmentType;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class GuardianAngel extends Enchantment {
    public GuardianAngel() {
        super(Rarity.VERY_RARE, CustomEnchantmentType.GAUNTLET, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean checkCompatibility(Enchantment p_77326_1_) {
        return super.checkCompatibility(p_77326_1_)
                && p_77326_1_ != EnchantmentRegistry.FLAMEBURST.get()
                && p_77326_1_ != EnchantmentRegistry.DRAGONBITE.get()
                && p_77326_1_ != EnchantmentRegistry.MARBLEGATE_LOOTING.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public int getMinCost(int p_77321_1_) {
        return 30;
    }

    @Override
    public int getMaxCost(int p_223551_1_) {
        return 60;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}
