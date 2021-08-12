package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import love.marblegate.risinguppercut.misc.ModGroup;
import love.marblegate.risinguppercut.misc.RotationUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.List;
import java.util.Map;

public class Gauntlet extends Item implements IVanishable {



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
            final int capTimer = Math.min((getUseDuration(stack) - timeLeft), SkillData.getRocketPunchMaxChangeTime(stack));
            rkp_cap.ifPresent(
                    cap -> {
                        cap.setTimer(capTimer);
                        cap.setDamage(SkillData.getRocketPunchDamagePerTick(stack) * capTimer);
                        cap.setSpeedIndex(SkillData.getRocketPunchSpeedIndex(stack));
                        cap.setKnockbackIndex(SkillData.getRocketPunchKnockbackSpeedIndex(stack));
                        cap.setDirection(RotationUtil.getHorizentalLookVecX(entityLiving), RotationUtil.getHorizentalLookVecZ(entityLiving));
                        cap.setHealing(SkillData.shouldHeal(stack));
                        cap.setIgnoreArmor(SkillData.shouldIgnoreArmor(stack));
                        cap.setIsFireDamage(SkillData.shouldBeFireDamage(stack));
                        cap.setShouldLoot(SkillData.shouldLoot(stack));
                    }
            );
            stack.damageItem(1, ((PlayerEntity) entityLiving), (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
            ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this,SkillData.getRocketPunchCooldown(stack));
            //Sync to client
            /*
            * it is not necessary now.
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayerEntity) entityLiving
                    ),
                    new PacketRocketPunchStatus(capTimer, SkillData.getRocketPunchDamagePerTick(stack),
                            SkillData.getRocketPunchSpeedIndex(stack),SkillData.getRocketPunchKnockbackSpeedIndex(stack),
                            RotationUtil.getHorizentalLookVecX(entityLiving), RotationUtil.getHorizentalLookVecZ(entityLiving),
                            SkillData.shouldIgnoreArmor(stack),SkillData.shouldHeal(stack),SkillData.shouldBeFireDamage(stack),SkillData.shouldLoot(stack)));

             */
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
        if (target.world.isRemote || hand == Hand.OFF_HAND) return ActionResultType.PASS;
        else{
            doRisingUppercut(playerIn.world,playerIn,stack);
            stack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            return ActionResultType.SUCCESS;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(handIn==Hand.MAIN_HAND){
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            //Work for rising uppercut
            if(playerIn.isSneaking()){
                if(!worldIn.isRemote()){
                    doRisingUppercut(worldIn,playerIn,itemstack);
                    itemstack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                }
                return ActionResult.resultSuccess(itemstack);
            }
            //Work for rocket punch
            else{
                playerIn.setActiveHand(handIn);
                return ActionResult.resultConsume(itemstack);
            }
        } else {
            return ActionResult.resultPass(playerIn.getHeldItemOffhand());
        }

    }

    void doRisingUppercut(World worldIn, PlayerEntity playerIn,ItemStack itemStack){
        //Slightly enlarge player's hitbox
        AxisAlignedBB collideBox = SkillData.shouldRisingUppercutAOE(itemStack)?
                playerIn.getBoundingBox().grow(2,0,2):
                playerIn.getBoundingBox().expand(RotationUtil.getHorizentalLookVecX(playerIn)*3,0,RotationUtil.getHorizentalLookVecZ(playerIn)*3);


        //Collision Detection
        List<LivingEntity> checks = playerIn.world
                .getEntitiesWithinAABB(LivingEntity.class,collideBox);
        checks.remove(playerIn);

        RisingUppercutWatcher watchEntity = new RisingUppercutWatcher(playerIn.world, playerIn.getPosition(),playerIn,
                SkillData.getRisingUppercutUpwardTime(itemStack), SkillData.getRisingUppercutFloatingTime(itemStack),
                SkillData.getRisingUppercutSpeedIndex(itemStack), SkillData.getRisingUppercutDamage(itemStack),
                SkillData.shouldIgnoreArmor(itemStack),SkillData.shouldHeal(itemStack),SkillData.shouldBeFireDamage(itemStack));
        if(!checks.isEmpty()){
            for(LivingEntity livingEntity:checks){
                watchEntity.watch(livingEntity);
            }
        }
        worldIn.addEntity(watchEntity);
        playerIn.getCooldownTracker().setCooldown(this,SkillData.getRisingUppercutCooldown(itemStack));
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    static class SkillData {
        static int ROCKET_PUNCH_BASE_MAX_CHANGE_TIME = Configuration.RocketPunchConfig.MAX_CHARGE_TIME.get();
        static float ROCKET_PUNCH_BASE_DAMAGE_PER_TICK = Configuration.RocketPunchConfig.DAMAGE.get().floatValue();
        static double ROCKET_PUNCH_BASE_SPEED_INDEX = Configuration.RocketPunchConfig.MOVEMENT_SPEED_INDEX.get();
        static double ROCKET_PUNCH_BASE_KNOCKBACK_SPEED_INDEX = Configuration.RocketPunchConfig.KNOCKBACK_SPEED_INDEX.get();
        static int ROCKET_PUNCH_BASE_COOLDOWN = Configuration.RocketPunchConfig.COOLDOWN.get();
        static int RISING_UPPERCUT_BASE_UPWARD_TIME = Configuration.RisingUppercutConfig.UPRISING_TIME.get();
        static int RISING_UPPERCUT_BASE_FLOATING_TIME = Configuration.RisingUppercutConfig.FLOATING_TIME.get();
        static float RISING_UPPERCUT_BASE_DAMAGE = Configuration.RisingUppercutConfig.DAMAGE.get().floatValue();
        static double RISING_UPPERCUT_BASE_SPEED_INDEX = Configuration.RisingUppercutConfig.RISING_SPEED_INDEX.get();
        static int RISING_UPPERCUT_BASE_COOLDOWN = Configuration.RisingUppercutConfig.COOLDOWN.get();

        public static int getRocketPunchMaxChangeTime(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.KADOKAWA_KINETIC_OPTIMIZATION.get()))
                return ROCKET_PUNCH_BASE_MAX_CHANGE_TIME + 4;
            else if(isItemEnchanted(itemStack, EnchantmentRegistry.MARBLEGATE_KINETIC_OPTIMIZATION.get()))
                return ROCKET_PUNCH_BASE_MAX_CHANGE_TIME + 12;
            return ROCKET_PUNCH_BASE_MAX_CHANGE_TIME;
        }

        public static float getRocketPunchDamagePerTick(ItemStack itemStack) {
            //No Enchantment Modifying This.
            return ROCKET_PUNCH_BASE_DAMAGE_PER_TICK;
        }

        public static double getRocketPunchSpeedIndex(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.ROCKET_PUNCH_CALCULATION_ASSIST.get()))
                return ROCKET_PUNCH_BASE_SPEED_INDEX * (1 + 0.3);
            return ROCKET_PUNCH_BASE_SPEED_INDEX;
        }

        public static double getRocketPunchKnockbackSpeedIndex(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.KADOKAWA_KINETIC_OPTIMIZATION.get()))
                return ROCKET_PUNCH_BASE_KNOCKBACK_SPEED_INDEX * (1 + 0.5);
            return ROCKET_PUNCH_BASE_KNOCKBACK_SPEED_INDEX;
        }

        public static int getRocketPunchCooldown(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.ROCKET_PUNCH_COOLING_ASSIST.get()))
                return ROCKET_PUNCH_BASE_COOLDOWN - 20;
            return ROCKET_PUNCH_BASE_COOLDOWN;
        }

        public static int getRisingUppercutUpwardTime(ItemStack itemStack) {
            //No Enchantment Modifying This.
            return RISING_UPPERCUT_BASE_UPWARD_TIME;
        }

        public static int getRisingUppercutFloatingTime(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.MARBLEGATE_KINETIC_OPTIMIZATION.get()))
                return RISING_UPPERCUT_BASE_FLOATING_TIME + 8;
            return RISING_UPPERCUT_BASE_FLOATING_TIME;
        }

        public static float getRisingUppercutDamage(ItemStack itemStack) {
            //No Enchantment Modifying This.
            return RISING_UPPERCUT_BASE_DAMAGE;
        }

        public static double getRisingUppercutSpeedIndex(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.RISING_UPPERCUT_CALCULATION_ASSIST.get()))
                return RISING_UPPERCUT_BASE_SPEED_INDEX * (1 + 0.3);
            return RISING_UPPERCUT_BASE_SPEED_INDEX;
        }

        public static int getRisingUppercutCooldown(ItemStack itemStack) {
            if(isItemEnchanted(itemStack, EnchantmentRegistry.RISING_UPPERCUT_COOLING_ASSIST.get()))
                return RISING_UPPERCUT_BASE_COOLDOWN - 20;
            return RISING_UPPERCUT_BASE_COOLDOWN;
        }

        public static boolean shouldRisingUppercutAOE(ItemStack itemStack) {
            return isItemEnchanted(itemStack, EnchantmentRegistry.AOE_ATTACK.get());
        }

        public static boolean shouldIgnoreArmor(ItemStack itemStack) {
            return isItemEnchanted(itemStack, EnchantmentRegistry.DRAGONBITE.get());
        }

        public static boolean shouldHeal(ItemStack itemStack) {
            return isItemEnchanted(itemStack, EnchantmentRegistry.GUARDIAN_ANGEL.get());
        }

        public static boolean shouldBeFireDamage(ItemStack itemStack) {
            return isItemEnchanted(itemStack, EnchantmentRegistry.FLAMEBURST.get());
        }

        public static int shouldLoot(ItemStack itemStack) {
            return getItemEnchantedLevel(itemStack, EnchantmentRegistry.MARBLEGATE_LOOTING.get());
        }

        static boolean isItemEnchanted(ItemStack itemStack, Enchantment enchantment){
            Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
            return enchantList.containsKey(enchantment);
        }

        static int getItemEnchantedLevel(ItemStack itemStack, Enchantment enchantment){
            Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
            return enchantList.getOrDefault(enchantment, 0);
        }
    }

}
