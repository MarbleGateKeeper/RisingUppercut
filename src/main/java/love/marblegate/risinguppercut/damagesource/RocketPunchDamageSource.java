package love.marblegate.risinguppercut.damagesource;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class RocketPunchDamageSource extends DamageSource {
    PlayerEntity playerEntity;

    public RocketPunchDamageSource(PlayerEntity playerEntity) {
        super("rocket_punch");
        this.playerEntity = playerEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        String s = "death.attack." + this.damageType;
        String sn = "death.attack." + this.damageType +".null_attacker" ;
        if(playerEntity!=null) return new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(),playerEntity);
        else return new TranslationTextComponent(sn, entityLivingBaseIn.getDisplayName());
    }
}
