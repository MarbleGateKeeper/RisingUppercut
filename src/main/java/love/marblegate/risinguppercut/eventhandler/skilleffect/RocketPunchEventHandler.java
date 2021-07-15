package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber()
public class RocketPunchEventHandler {

    @SubscribeEvent
    public static void goStraightForward(TickEvent.PlayerTickEvent event){
        if(!event.player.world.isRemote()){
            //Deal with rocket punch action
            if(event.phase == TickEvent.Phase.START){
                LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = event.player.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);

                //Slightly enlarge player's hitbox
                AxisAlignedBB collideBox = event.player.getBoundingBox().grow(0.25f,0,0.25f);

                //Collision Detection
                List<LivingEntity> checks = event.player.world
                        .getEntitiesWithinAABB(LivingEntity.class,collideBox);
                checks.remove(event.player);

                //If any mob is detected
                if (!checks.isEmpty()) {
                    rkp_cap.ifPresent(
                            cap-> {

                                //And rocket punch is active
                                if(cap.getTimer()>0){
                                    for(LivingEntity target: checks){
                                        //Deal damage
                                        target.attackEntityFrom(new RocketPunchDamageSource(event.player.getDisplayName().toString()),cap.getStrength()*0.5f);

                                        //spawn an entity to simulate rocket punch effect



                                    }

                                    //Player stop moving and clear pocket punch status
                                    event.player.setMotion(0,0,0);
                                    event.player.markPositionDirty();
                                    event.player.velocityChanged = true;
                                    cap.clear();
                                }
                            }
                    );
                }

                //If rocket punch is active and player hit a wall
                //stop player and clear rocket punch status
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
                            //Deal with player rocket punch movement
                            if(cap.getTimer()>0){
                                //lock player view
                                event.player.lookAt(EntityAnchorArgument.Type.EYES,event.player.getPositionVec().add(cap.getDirectionX(),event.player.getEyeHeight(),cap.getDirectionZ()));

                                //lock moving direction
                                event.player.setMotion(cap.getDirectionX()*2f,0,cap.getDirectionZ()*2f);

                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;

                                cap.decrease();

                            /*
                            Should we send packet here?
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
}
