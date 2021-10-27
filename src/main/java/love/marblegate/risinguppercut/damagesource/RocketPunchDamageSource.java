package love.marblegate.risinguppercut.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class RocketPunchDamageSource extends EntityDamageSource {

    public RocketPunchDamageSource(Player playerEntity) {
        super("rising_uppercut.rocket_punch", playerEntity);
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        String s = "death.attack." + msgId;
        return new TranslatableComponent(s, entityLivingBaseIn.getDisplayName(), entity);
    }
}
