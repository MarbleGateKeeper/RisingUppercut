package love.marblegate.risinguppercut.item;

import love.marblegate.risinguppercut.entity.watcher.RisingUppercutWatcher;
import love.marblegate.risinguppercut.entity.watcher.RocketPunchProcessWatcher;
import love.marblegate.risinguppercut.misc.Configuration;
import love.marblegate.risinguppercut.misc.ModCreativeTab;
import love.marblegate.risinguppercut.misc.RotationUtil;
import love.marblegate.risinguppercut.registry.MobEffectRegistry;
import love.marblegate.risinguppercut.registry.EnchantmentRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class Gauntlet extends Item implements Vanishable {


    public Gauntlet() {
        super(new Properties()
                .tab(ModCreativeTab.INSTANCE)
                .stacksTo(1)
                .durability(1024)
                .fireResistant());
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!level.isClientSide) {
            final int capTimer = Math.min((getUseDuration(stack) - timeLeft), SkillData.getRocketPunchMaxChangeTime(stack));
            RocketPunchProcessWatcher initializer = new RocketPunchProcessWatcher(entityLiving.level, entityLiving.blockPosition(), capTimer,
                    SkillData.getRocketPunchKnockbackSpeedIndex(stack), SkillData.getRocketPunchSpeedIndex(stack), SkillData.getRocketPunchDamagePerTick(stack),
                    RotationUtil.getHorizentalLookVecX(entityLiving), RotationUtil.getHorizentalLookVecZ(entityLiving),
                    SkillData.shouldIgnoreArmor(stack), SkillData.shouldHeal(stack), SkillData.shouldBeFireDamage(stack),
                    (Player) entityLiving, SkillData.shouldLoot(stack));
            level.addFreshEntity(initializer);

            stack.hurtAndBreak(1, ((Player) entityLiving), (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            ((Player) entityLiving).getCooldowns().addCooldown(this, SkillData.getRocketPunchCooldown(stack));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target.level.isClientSide || hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;
        else {
            doRisingUppercut(playerIn.level, playerIn, stack);
            stack.hurtAndBreak(1, playerIn, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND) {
            ItemStack itemstack = playerIn.getItemInHand(handIn);
            //Work for rising uppercut
            if (playerIn.isShiftKeyDown()) {
                if (!level.isClientSide()) {
                    doRisingUppercut(level, playerIn, itemstack);
                    itemstack.hurtAndBreak(1, playerIn, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
                return InteractionResultHolder.success(itemstack);
            }
            //Work for rocket punch
            else {
                playerIn.startUsingItem(handIn);
                return InteractionResultHolder.consume(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(playerIn.getOffhandItem());
        }

    }

    //Execute Rising Uppercut
    void doRisingUppercut(Level worldIn, Player playerIn, ItemStack itemStack) {
        //Slightly enlarge player's hitbox
        AABB collideBox = SkillData.shouldRisingUppercutAOE(itemStack) ?
                playerIn.getBoundingBox().inflate(2, 0, 2) :
                playerIn.getBoundingBox().expandTowards(RotationUtil.getHorizentalLookVecX(playerIn) * 3, 0, RotationUtil.getHorizentalLookVecZ(playerIn) * 3);


        //Collision Detection
        List<LivingEntity> checks = playerIn.level
                .getEntitiesOfClass(LivingEntity.class, collideBox);
        checks.remove(playerIn);

        RisingUppercutWatcher watchEntity = new RisingUppercutWatcher(playerIn.level, playerIn.blockPosition(), playerIn,
                SkillData.getRisingUppercutUpwardTime(itemStack), SkillData.getRisingUppercutFloatingTime(itemStack),
                SkillData.getRisingUppercutSpeedIndex(itemStack), SkillData.getRisingUppercutDamage(itemStack),
                SkillData.shouldIgnoreArmor(itemStack), SkillData.shouldHeal(itemStack), SkillData.shouldBeFireDamage(itemStack));
        if (!checks.isEmpty()) {
            for (LivingEntity livingEntity : checks) {
                watchEntity.watch(livingEntity);
            }
        }
        worldIn.addFreshEntity(watchEntity);
        if (SkillData.shouldApplySoftLanding(itemStack)) {
            playerIn.addEffect(new MobEffectInstance(MobEffectRegistry.SAFE_LANDING.get(), SkillData.getSoftLandingDefaultDuration(itemStack)));
        }
        playerIn.getCooldowns().addCooldown(this, SkillData.getRisingUppercutCooldown(itemStack));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
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
