package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.IRocketPunchMobHitRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord.RocketPunchMobHitRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.registry.EffectRegistry;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber()
public class RocketPunchEventHandler {

    @SubscribeEvent
    public static void goStraightForward(TickEvent.PlayerTickEvent event){

        //处理玩家火箭重拳
        if(event.phase == TickEvent.Phase.START){
            LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = event.player.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);

            //略微放大玩家碰撞箱
            AxisAlignedBB collideBox = event.player.getBoundingBox().grow(0.25f,0,0.25f);

            //检测玩家碰撞的生物
            List<LivingEntity> checks = event.player.world
                    .getEntitiesWithinAABB(LivingEntity.class,collideBox);
            if(checks.contains(event.player)) checks.remove(event.player);

            //如果有碰撞到生物
            if (!checks.isEmpty()) {
                rkp_cap.ifPresent(
                        cap-> {

                            //且技能处于激活中
                            if(cap.getTimer()>0){
                                for(LivingEntity target: checks){
                                    //造成伤害
                                    target.attackEntityFrom(new RocketPunchDamageSource(event.player.getDisplayName().toString()),cap.getStrength()*0.5f);

                                    //设置火箭冲拳击退的处理effect
                                    target.addPotionEffect(new EffectInstance(EffectRegistry.rocket_punch_hit.get(),200));

                                    //击退
                                    //target.applyKnockback(cap.getStrength()*0.25f,-cap.getDirectionX(),-cap.getDirectionZ());

                                    //能力
                                    LazyOptional<IRocketPunchMobHitRecord> rkp_hit_cap = target.getCapability(RocketPunchMobHitRecord.ROCKET_PUNCH_MOB_HIT_RECORD);
                                    rkp_hit_cap.ifPresent(
                                            hit_cap-> {
                                                hit_cap.setStrength(cap.getStrength());
                                                hit_cap.setAttackerName(event.player.getDisplayName().toString());
                                                hit_cap.setDirection(cap.getDirectionX(),cap.getDirectionZ());
                                            }
                                    );

                                }

                                //那么停止玩家移动并清空cap
                                event.player.setMotion(0,0,0);
                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;
                                cap.clear();
                            }
                        }
                );
            }

            //如果技能处于激活中，如果玩家撞墙，那么停止玩家移动并清空cap
            if(event.player.collidedHorizontally){
                rkp_cap.ifPresent(
                        cap-> {
                            if(cap.getTimer()>0){
                                event.player.setMotion(0,0,0);
                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;
                                cap.clear();
                            }
                        }
                );
            }
            rkp_cap.ifPresent(
                    cap-> {

                        //此处处理玩家技能激活时的移动
                        if(cap.getTimer()>0){
                            //锁定玩家视角
                            event.player.lookAt(EntityAnchorArgument.Type.EYES,event.player.getPositionVec().add(cap.getDirectionX(),event.player.getEyeHeight(),cap.getDirectionZ()));

                            //处理玩家移动，锁定移动方向
                            event.player.setMotion(cap.getDirectionX()*2f,0,cap.getDirectionZ()*2f);
                            event.player.markPositionDirty();
                            event.player.velocityChanged = true;

                            cap.decrease();

                            /*
                            这个部分真的要发包同步吗？
                            if(!event.player.world.isRemote()) {
                                Networking.INSTANCE.send(
                                        PacketDistributor.PLAYER.with(
                                                () -> (ServerPlayerEntity) event.player
                                        ),
                                        new PacketRocketPunch(cap.getTimer(),cap.getStrength(),cap.getDirectionX(),cap.getDirectionZ()));
                            }
                             */
                        }
                    }
            );
        }
    }
}
