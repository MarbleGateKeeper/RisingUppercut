package love.marblegate.risinguppercut.misc.proxy;

import net.minecraft.potion.Effect;

public interface IProxy {
    default void removeEffect(Effect effect){}

}
