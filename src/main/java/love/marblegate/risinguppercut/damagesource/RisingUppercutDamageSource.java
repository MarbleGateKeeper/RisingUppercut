package love.marblegate.risinguppercut.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class RisingUppercutDamageSource extends DamageSource {
    PlayerEntity playerEntity;

    public RisingUppercutDamageSource(PlayerEntity playerEntity) {
        super("rising_uppercut");
        this.playerEntity = playerEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        String s = "death.attack." + this.damageType;
        return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(),playerEntity);
    }
}
