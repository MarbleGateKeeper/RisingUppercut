package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.network.Networking;
import love.marblegate.risinguppercut.network.PacketRocketPunchStatus;
import love.marblegate.risinguppercut.util.RotationUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

public class Gauntlet extends Item {

    public static class SkillConstants{
        public static int ROCKET_PUNCH_MAX_STRENGTH = 26;
    }

    public Gauntlet() {
        super(new Properties()
                .group(ItemGroup.COMBAT)
                .maxStackSize(1)
                .isImmuneToFire());
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft){
        LazyOptional<IRocketPunchPlayerSkillRecord> rkp_cap = entityLiving.getCapability(RocketPunchPlayerSkillRecord.ROCKET_PUNCH_SKILL_RECORD);
        final int capTimer = Math.min((this.getUseDuration(stack) - timeLeft), SkillConstants.ROCKET_PUNCH_MAX_STRENGTH);
        rkp_cap.ifPresent(
                cap-> {
                    cap.setTimer(capTimer);
                    cap.setStrength(capTimer);
                    cap.setDirection(RotationUtil.getHorizentalLookVecX(entityLiving),RotationUtil.getHorizentalLookVecZ(entityLiving));
                }
        );
        if (!worldIn.isRemote) {
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayerEntity) entityLiving
                    ),
                    new PacketRocketPunchStatus(capTimer,capTimer,RotationUtil.getHorizentalLookVecX(entityLiving),RotationUtil.getHorizentalLookVecZ(entityLiving)));
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

}
