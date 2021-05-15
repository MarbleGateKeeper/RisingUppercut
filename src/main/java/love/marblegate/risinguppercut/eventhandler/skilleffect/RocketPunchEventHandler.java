package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.capability.rocketpunch.IRocketPunchIndicator;
import love.marblegate.risinguppercut.capability.rocketpunch.RocketPunchIndicator;
import love.marblegate.risinguppercut.network.Networking;
import love.marblegate.risinguppercut.network.PacketRocketPunch;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber()
public class RocketPunchEventHandler {

    @SubscribeEvent
    public static void goStraightForward(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            LazyOptional<IRocketPunchIndicator> rkp_cap = event.player.getCapability(RocketPunchIndicator.ROCKET_PUNCH_INDICATOR);
            AxisAlignedBB collideBox = event.player.getBoundingBox().grow(0.25f,0,0.25f);
            List<LivingEntity> checks = event.player.world
                    .getEntitiesWithinAABB(LivingEntity.class,collideBox);
            if(checks.contains(event.player)) checks.remove(event.player);
            if (!checks.isEmpty()) {
                rkp_cap.ifPresent(
                        cap-> {
                            if(cap.getTimer()>0){
                                for(LivingEntity target: checks){
                                    target.applyKnockback(cap.getStrength()*0.75f,-cap.getDirectionX(),-cap.getDirectionZ());
                                }
                                event.player.setMotion(0,0,0);
                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;
                                cap.clear();
                            }
                        }
                );
            }
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
                        if(cap.getTimer()>0){
                            event.player.setMotion(cap.getDirectionX()*2f,0,cap.getDirectionZ()*2f);
                            event.player.markPositionDirty();
                            event.player.velocityChanged = true;
                            cap.decrease();
                            if(!event.player.world.isRemote()) {
                                Networking.INSTANCE.send(
                                        PacketDistributor.PLAYER.with(
                                                () -> (ServerPlayerEntity) event.player
                                        ),
                                        new PacketRocketPunch(cap.getTimer(),cap.getStrength(),cap.getDirectionX(),cap.getDirectionZ()));
                            }
                        }
                    }
            );
        }
    }
}
