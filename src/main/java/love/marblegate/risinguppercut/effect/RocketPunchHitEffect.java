package love.marblegate.risinguppercut.effect;

import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.IRocketPunchMobHitRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.RocketPunchMobHitRecord;
import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.damagesource.RocketPunchOnWallDamageSource;
import love.marblegate.risinguppercut.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;

public class RocketPunchHitEffect extends Effect {
    public RocketPunchHitEffect() {
        super(EffectType.HARMFUL, 0);
    }

    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        //检测是否撞到墙面上
        if (this == EffectRegistry.rocket_punch_hit.get()){
            if(entityLivingBaseIn.collidedHorizontally)
            {
                LazyOptional<IRocketPunchMobHitRecord> rkp_hit_cap = entityLivingBaseIn.getCapability(RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD);
                rkp_hit_cap.ifPresent(
                        cap-> {
                            //制造伤害
                            entityLivingBaseIn.attackEntityFrom(new RocketPunchOnWallDamageSource(cap.getAttackerName()),cap.getStrength()*0.5f);

                            //清空Cap数据
                            cap.clear();
                        }
                );
            }
        }

    }

    public boolean isReady(int duration, int amplifier) {
        if(duration>0) return true;
        else return false;
    }


    public boolean shouldRender(EffectInstance effect) { return false; }

    public boolean shouldRenderHUD(EffectInstance effect) { return false; }

    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }
}
