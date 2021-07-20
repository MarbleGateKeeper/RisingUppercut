package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.damagesource.RisingUppercutDamageSource;
import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.network.Networking;
import love.marblegate.risinguppercut.network.PacketRocketPunchStatus;
import love.marblegate.risinguppercut.util.ModGroup;
import love.marblegate.risinguppercut.util.RotationUtil;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class Gauntlet extends Item implements IVanishable {

    public static class SkillConstants{
        public static int ROCKET_PUNCH_MAX_STRENGTH = 20;
        public static int RISING_UPPERCUT_CONTROL_TIME = 12;
    }

    public Gauntlet() {
        super(new Properties()
                .group(ModGroup.INSTANCE)
                .maxStackSize(1)
                .maxDamage(1024)
                .isImmuneToFire());
    }


    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!worldIn.isRemote) {
            LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = entityLiving.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);
            final int capTimer = Math.min((getUseDuration(stack) - timeLeft), SkillConstants.ROCKET_PUNCH_MAX_STRENGTH);
            rkp_cap.ifPresent(
                    cap -> {
                        cap.setTimer(capTimer);
                        cap.setStrength(capTimer);
                        cap.setDirection(RotationUtil.getHorizentalLookVecX(entityLiving), RotationUtil.getHorizentalLookVecZ(entityLiving));
                    }
            );
            stack.damageItem(1, ((PlayerEntity) entityLiving), (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
            ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this,40);
            //Sync to client
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayerEntity) entityLiving
                    ),
                    new PacketRocketPunchStatus(capTimer, capTimer, RotationUtil.getHorizentalLookVecX(entityLiving), RotationUtil.getHorizentalLookVecZ(entityLiving)));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 18;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target.world.isRemote) return ActionResultType.PASS;
        else{
            doRisingUppercut(playerIn.world,playerIn);
            stack.damageItem(1, playerIn, (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
            return ActionResultType.SUCCESS;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        //Work for rising uppercut
        if(playerIn.isSneaking()){
            if(!worldIn.isRemote()){
                doRisingUppercut(worldIn,playerIn);
                itemstack.damageItem(1, playerIn, (entity) -> {
                    entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            }
            return ActionResult.resultSuccess(itemstack);
        }
        //Work for rocket punch
        else{
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
    }

    void doRisingUppercut(World worldIn, PlayerEntity playerIn){
        //Slightly enlarge player's hitbox
        AxisAlignedBB collideBox = playerIn.getBoundingBox().expand(RotationUtil.getHorizentalLookVecX(playerIn)*3,0,RotationUtil.getHorizentalLookVecZ(playerIn)*3);

        //Collision Detection
        List<LivingEntity> checks = playerIn.world
                .getEntitiesWithinAABB(LivingEntity.class,collideBox);
        checks.remove(playerIn);

        RisingUppercutWatcher watchEntity = new RisingUppercutWatcher(playerIn.world, playerIn.getPosition(),SkillConstants.RISING_UPPERCUT_CONTROL_TIME,playerIn);
        if(!checks.isEmpty()){
            for(LivingEntity livingEntity:checks){
                livingEntity.attackEntityFrom(new RisingUppercutDamageSource(playerIn),8);
                watchEntity.watch(livingEntity);
            }
        }
        worldIn.addEntity(watchEntity);
        playerIn.getCooldownTracker().setCooldown(this,40);
    }



    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

}
