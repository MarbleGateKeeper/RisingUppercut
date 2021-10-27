package love.marblegate.risinguppercut.misc.proxy;

import net.minecraft.world.effect.MobEffect;

public interface IProxy {
    default void removeEffect(MobEffect mobEffect) {
    }

}
