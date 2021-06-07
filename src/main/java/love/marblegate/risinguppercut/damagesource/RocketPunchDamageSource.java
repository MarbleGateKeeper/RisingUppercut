package love.marblegate.risinguppercut.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class RocketPunchDamageSource extends DamageSource {
    String damageSourceEntityName;

    public RocketPunchDamageSource(String damageSourceEntityName) {
        super("rocket_punch");
        this.damageSourceEntityName = damageSourceEntityName;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        String s = "death.attack." + this.damageType;
        return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(),damageSourceEntityName);
    }
}
