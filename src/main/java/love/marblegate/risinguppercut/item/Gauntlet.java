package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.IRocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord.RocketPunchPlayerSkillRecord;
import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.misc.ModGroup;
import love.marblegate.risinguppercut.misc.RotationUtil;
import love.marblegate.risinguppercut.registry.EffectRegistry;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
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
            ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, SkillData.getRocketPunchCooldown(stack));
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
        else {
            doRisingUppercut(playerIn.world, playerIn, stack);
            stack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            return ActionResultType.SUCCESS;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn == Hand.MAIN_HAND) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            //Work for rising uppercut
            if (playerIn.isSneaking()) {
                if (!worldIn.isRemote()) {
                    doRisingUppercut(worldIn, playerIn, itemstack);
                    itemstack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                }
                return ActionResult.resultSuccess(itemstack);
            }
            //Work for rocket punch
            else {
                playerIn.setActiveHand(handIn);
                return ActionResult.resultConsume(itemstack);
            }
        } else {
            return ActionResult.resultPass(playerIn.getHeldItemOffhand());
        }

    }

    //Execute Rising Uppercut
    void doRisingUppercut(World worldIn, PlayerEntity playerIn, ItemStack itemStack) {
        //Slightly enlarge player's hitbox
        AxisAlignedBB collideBox = SkillData.shouldRisingUppercutAOE(itemStack) ?
                playerIn.getBoundingBox().grow(2, 0, 2) :
                playerIn.getBoundingBox().expand(RotationUtil.getHorizentalLookVecX(playerIn) * 3, 0, RotationUtil.getHorizentalLookVecZ(playerIn) * 3);


        //Collision Detection
        List<LivingEntity> checks = playerIn.world
                .getEntitiesWithinAABB(LivingEntity.class, collideBox);
        checks.remove(playerIn);

        RisingUppercutWatcher watchEntity = new RisingUppercutWatcher(playerIn.world, playerIn.getPosition(), playerIn,
                SkillData.getRisingUppercutUpwardTime(itemStack), SkillData.getRisingUppercutFloatingTime(itemStack),
                SkillData.getRisingUppercutSpeedIndex(itemStack), SkillData.getRisingUppercutDamage(itemStack),
                SkillData.shouldIgnoreArmor(itemStack), SkillData.shouldHeal(itemStack), SkillData.shouldBeFireDamage(itemStack));
        if (!checks.isEmpty()) {
            for (LivingEntity livingEntity : checks) {
                watchEntity.watch(livingEntity);
            }
        }
        worldIn.addEntity(watchEntity);
        if (SkillData.shouldApplySoftLanding(itemStack)) {
            playerIn.addPotionEffect(new EffectInstance(EffectRegistry.SAFE_LANDING.get(), SkillData.getSoftLandingDefaultDuration(itemStack)));
        }
        playerIn.getCooldownTracker().setCooldown(this, SkillData.getRisingUppercutCooldown(itemStack));
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    static class SkillData {

        public static int getRocketPunchMaxChangeTime(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.KADOKAWA_KINETIC_OPTIMIZATION.get()))
                return Configuration.RocketPunchConfig.MAX_CHARGE_TIME.get() + 4;
            else if (isItemEnchanted(itemStack, EnchantmentRegistry.MARBLEGATE_KINETIC_OPTIMIZATION.get()))
                return Configuration.RocketPunchConfig.MAX_CHARGE_TIME.get() + 12;
            return Configuration.RocketPunchConfig.MAX_CHARGE_TIME.get();
        }

        public static float getRocketPunchDamagePerTick(ItemStack itemStack) {
            //No Enchantment Modifying This.
            return Configuration.RocketPunchConfig.DAMAGE.get().floatValue();
        }

        public static double getRocketPunchSpeedIndex(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.ROCKET_PUNCH_CALCULATION_ASSIST.get()))
                return Configuration.RocketPunchConfig.MOVEMENT_SPEED_INDEX.get() * (1 + 0.3);
            return Configuration.RocketPunchConfig.MOVEMENT_SPEED_INDEX.get();
        }

        public static double getRocketPunchKnockbackSpeedIndex(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.KADOKAWA_KINETIC_OPTIMIZATION.get()))
                return Configuration.RocketPunchConfig.KNOCKBACK_SPEED_INDEX.get() * (1 + 0.5);
            return Configuration.RocketPunchConfig.KNOCKBACK_SPEED_INDEX.get();
        }

        public static int getRocketPunchCooldown(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.ROCKET_PUNCH_COOLING_ASSIST.get()))
                return Configuration.RocketPunchConfig.COOLDOWN.get() - 20;
            return Configuration.RocketPunchConfig.COOLDOWN.get();
        }

        public static int getRisingUppercutUpwardTime(ItemStack itemStack) {
            //No Enchantment Modifying This.
            return Configuration.RisingUppercutConfig.UPRISING_TIME.get();
        }

        public static int getRisingUppercutFloatingTime(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.MARBLEGATE_KINETIC_OPTIMIZATION.get()))
                return Configuration.RisingUppercutConfig.FLOATING_TIME.get() + 8;
            return Configuration.RisingUppercutConfig.FLOATING_TIME.get();
        }

        public static float getRisingUppercutDamage(ItemStack itemStack) {
            //Nothing Modifying This.
            return Configuration.RisingUppercutConfig.DAMAGE.get().floatValue();
        }

        public static double getRisingUppercutSpeedIndex(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.RISING_UPPERCUT_CALCULATION_ASSIST.get()))
                return Configuration.RisingUppercutConfig.RISING_SPEED_INDEX.get() * (1 + 0.3);
            return Configuration.RisingUppercutConfig.RISING_SPEED_INDEX.get();
        }

        public static int getRisingUppercutCooldown(ItemStack itemStack) {
            if (isItemEnchanted(itemStack, EnchantmentRegistry.RISING_UPPERCUT_COOLING_ASSIST.get()))
                return Configuration.RisingUppercutConfig.COOLDOWN.get() - 20;
            return Configuration.RisingUppercutConfig.COOLDOWN.get();
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

        public static boolean shouldApplySoftLanding(ItemStack itemStack) {
            if (Configuration.SafeLandingConfig.DO_NOT_REQUIRE_ENCHANTMENT.get()) return true;
            else return isItemEnchanted(itemStack, EnchantmentRegistry.SOFTFALLING.get());
        }

        public static int getSoftLandingDefaultDuration(ItemStack itemStack) {
            //Nothing Modifying This.
            return Configuration.SafeLandingConfig.DEFAULT_DURATION.get();
        }

        static boolean isItemEnchanted(ItemStack itemStack, Enchantment enchantment) {
            Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
            return enchantList.containsKey(enchantment);
        }

        static int getItemEnchantedLevel(ItemStack itemStack, Enchantment enchantment) {
            Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
            return enchantList.getOrDefault(enchantment, 0);
        }
    }

}
