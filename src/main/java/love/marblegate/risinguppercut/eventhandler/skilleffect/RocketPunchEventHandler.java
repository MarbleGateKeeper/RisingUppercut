package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchWatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                AxisAlignedBB collideBox = event.player.getBoundingBox().grow(0.5f,0,0.5f);

                //Collision Detection
                List<LivingEntity> checks = event.player.world
                        .getEntitiesWithinAABB(LivingEntity.class,collideBox);
                checks.remove(event.player);

                //If any mob is detected
                if (!checks.isEmpty()) {
                    rkp_cap.ifPresent(
                            cap-> {
                                // And rocket punch is active
                                if(cap.getTimer()>0){
                                    // spawn an watchEntity to simulate rocket punch effect
                                    RocketPunchWatcher watchEntity = new RocketPunchWatcher(event.player.world, event.player.getPosition(),cap.getTimer(),
                                            cap.getKnockbackSpeedIndex(),cap.getDamage(), cap.getDirectionX(),cap.getDirectionZ(),
                                            cap.ignoreArmor(),cap.healing(),cap.isFireDamage(),event.player);
                                    for(LivingEntity target: checks){
                                        if(target instanceof PlayerEntity){
                                            LazyOptional<IRocketPunchPlayerSkillRecord> another_cap = target.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);
                                            another_cap.ifPresent( other_cap -> {
                                                if(other_cap.getTimer()>0){
                                                    // If two player who is using Rocket Punch Collide.
                                                    // Stop them and apply knockback
                                                    target.setMotion(0,0,0);
                                                    target.markPositionDirty();
                                                    target.velocityChanged = true;
                                                    target.applyKnockback(3,-other_cap.getDirectionX(),-other_cap.getDirectionZ());
                                                    other_cap.clear();
                                                    event.player.setMotion(0,0,0);
                                                    event.player.markPositionDirty();
                                                    event.player.velocityChanged = true;
                                                    target.applyKnockback(3,-cap.getDirectionX(),-cap.getDirectionZ());
                                                    cap.clear();
                                                    // Then do not run the following codes
                                                    return;

                                                }
                                            });
                                        }
                                        // Deal damage
                                        DamageSource damageSource = new RocketPunchDamageSource(event.player);
                                        if(cap.shouldLoot()>0){
                                            //TODO: Use custom recipe in the future
                                            // - REALLY F**KING LOW EFFICIENCY HERE
                                            dropLoot(target,DamageSource.causePlayerDamage(event.player),true,event.player);
                                        }
                                        if(cap.isFireDamage()){
                                            damageSource.setFireDamage();
                                            target.setFire(3);
                                            target.attackEntityFrom(damageSource,cap.getDamage());
                                        } else if(cap.ignoreArmor()){
                                            damageSource.setDamageBypassesArmor();
                                            target.attackEntityFrom(damageSource,cap.getDamage());
                                        } else if(cap.healing()){
                                            target.heal(cap.getDamage());
                                        } else {
                                            target.attackEntityFrom(damageSource,cap.getDamage());
                                        }

                                        if(target.isAlive()){
                                            watchEntity.watch(target);
                                        }
                                    }
                                    event.player.world.addEntity(watchEntity);

                                    // Player stop moving and clear pocket punch status
                                    event.player.setMotion(0,0,0);
                                    event.player.markPositionDirty();
                                    event.player.velocityChanged = true;
                                    cap.clear();
                                }
                            }
                    );
                }

                // If rocket punch is active and player hit a wall
                // stop player and clear rocket punch status
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
                            // Deal with player rocket punch movement
                            if(cap.getTimer()>0){

                                // lock moving direction
                                event.player.setMotion(cap.getDirectionX()*cap.getSpeedIndex(),0.1,cap.getDirectionZ()*cap.getSpeedIndex());

                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;

                                cap.decrease();
                            }
                        }
                );
            }
        }
    }

    //FIXME TEMPORARY USAGE ONLY
    static void dropLoot(LivingEntity livingEntity, DamageSource damageSourceIn, boolean attackedRecently, PlayerEntity playerEntity) {
        ResourceLocation resourcelocation = livingEntity.getLootTableResourceLocation();
        LootTable loottable = livingEntity.world.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
        LootContext.Builder lootcontext$builder = getLootContextBuilder(livingEntity,attackedRecently, damageSourceIn, playerEntity);
        LootContext ctx = lootcontext$builder.build(LootParameterSets.ENTITY);
        loottable.generate(ctx).forEach(livingEntity::entityDropItem);
    }

    static LootContext.Builder getLootContextBuilder(LivingEntity livingEntity, boolean attackedRecently, DamageSource damageSourceIn, PlayerEntity playerEntity) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) livingEntity.world)).withRandom(livingEntity.getRNG()).withParameter(LootParameters.THIS_ENTITY, livingEntity).withParameter(LootParameters.ORIGIN, livingEntity.getPositionVec()).withParameter(LootParameters.DAMAGE_SOURCE, damageSourceIn).withNullableParameter(LootParameters.KILLER_ENTITY, damageSourceIn.getTrueSource()).withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, damageSourceIn.getImmediateSource());
        if (attackedRecently && playerEntity != null) {
            lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());
        }
        return lootcontext$builder;
    }
}
