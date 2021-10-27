package love.marblegate.risinguppercut.misc.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;

public class ClientProxy implements IProxy {

    @Override
    public void removeEffect(MobEffect mobEffect) {
        Minecraft.getInstance().player.removeEffectNoUpdate(mobEffect);
    }
}
