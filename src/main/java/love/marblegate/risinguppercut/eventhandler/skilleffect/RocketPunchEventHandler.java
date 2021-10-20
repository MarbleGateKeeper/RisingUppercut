package love.marblegate.risinguppercut.eventhandler.skilleffect;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.damagesource.RocketPunchDamageSource;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchWatcher;
import love.marblegate.risinguppercut.misc.LootUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber()
public class RocketPunchEventHandler {

    @SubscribeEvent
    public static void goStraightForward(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote()) {
            //Deal with rocket punch action
            if (event.phase == TickEvent.Phase.START) {
                LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = event.player.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);

                //Slightly enlarge player's hitbox
                AxisAlignedBB collideBox = event.player.getBoundingBox().grow(0.5f, 0, 0.5f);

                //Collision Detection
                List<LivingEntity> checks = event.player.world
                        .getEntitiesWithinAABB(LivingEntity.class, collideBox);
                checks.remove(event.player);

                //If any mob is detected
                if (!checks.isEmpty()) {
                    rkp_cap.ifPresent(
                            cap -> {
                                // And rocket punch is active
                                if (cap.getTimer() > 0) {
                                    // spawn an watchEntity to simulate rocket punch effect
                                    RocketPunchWatcher watchEntity = new RocketPunchWatcher(event.player.world, event.player.getPosition(), cap.getTimer(), cap.getEffectiveChargeTime(),
                                            cap.getKnockbackSpeedIndex(), cap.getDamagePerEffectiveCharge(), cap.getDirectionX(), cap.getDirectionZ(), cap.ignoreArmor(), cap.healing(), cap.isFireDamage(), event.player);
                                    for (LivingEntity target : checks) {
                                        // Deal damage
                                        DamageSource damageSource = new RocketPunchDamageSource(event.player);
                                        if (cap.shouldLoot() > 0) {
                                            LootUtil.dropLoot(target, DamageSource.causePlayerDamage(event.player), true, event.player);
                                        }
                                        if (cap.isFireDamage()) {
                                            damageSource.setFireDamage();
                                            target.setFire(3);
                                            target.attackEntityFrom(damageSource, cap.getDamagePerEffectiveCharge() * cap.getEffectiveChargeTime());
                                        } else if (cap.ignoreArmor()) {
                                            damageSource.setDamageBypassesArmor();
                                            target.attackEntityFrom(damageSource, cap.getDamagePerEffectiveCharge() * cap.getEffectiveChargeTime());
                                        } else if (cap.healing()) {
                                            target.heal(cap.getDamagePerEffectiveCharge()  * cap.getEffectiveChargeTime());
                                        } else {
                                            target.attackEntityFrom(damageSource, cap.getDamagePerEffectiveCharge() * cap.getEffectiveChargeTime());
                                        }

                                        if (target.isAlive()) {
                                            watchEntity.watch(target);
                                        }
                                    }
                                    event.player.world.addEntity(watchEntity);

                                    // Player stop moving and clear pocket punch status
                                    event.player.setMotion(0, 0, 0);
                                    event.player.markPositionDirty();
                                    event.player.velocityChanged = true;
                                    cap.clear();
                                }
                            }
                    );
                }

                // If rocket punch is active and player hit a wall
                // stop player and clear rocket punch status
                if (event.player.collidedHorizontally) {
                    rkp_cap.ifPresent(
                            cap -> {
                                if (cap.getTimer() > 0) {
                                    event.player.setMotion(0, 0, 0);
                                    event.player.markPositionDirty();
                                    event.player.velocityChanged = true;
                                    cap.clear();
                                }
                            }
                    );
                }
                rkp_cap.ifPresent(
                        cap -> {
                            // Deal with player rocket punch movement
                            if (cap.getTimer() > 0) {

                                // lock moving direction
                                event.player.setMotion(cap.getDirectionX() * cap.getSpeedIndex(), 0.1, cap.getDirectionZ() * cap.getSpeedIndex());

                                event.player.markPositionDirty();
                                event.player.velocityChanged = true;

                                cap.decrease();
                            }
                        }
                );
            }
        }
    }
}
