package love.marblegate.risinguppercut.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

//TODO I believe we need something more reliable and predictable
public class LootUtil {
    public static void dropLoot(LivingEntity livingEntity, DamageSource damageSourceIn, boolean attackedRecently, PlayerEntity playerEntity) {
        ResourceLocation resourcelocation = livingEntity.getLootTableResourceLocation();
        LootTable loottable = livingEntity.world.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
        LootContext.Builder lootcontext$builder = getLootContextBuilder(livingEntity, attackedRecently, damageSourceIn, playerEntity);
        LootContext ctx = lootcontext$builder.build(LootParameterSets.ENTITY);
        loottable.generate(ctx).forEach(livingEntity::entityDropItem);
    }

    public static LootContext.Builder getLootContextBuilder(LivingEntity livingEntity, boolean attackedRecently, DamageSource damageSourceIn, PlayerEntity playerEntity) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) livingEntity.world)).withRandom(livingEntity.getRNG()).withParameter(LootParameters.THIS_ENTITY, livingEntity).withParameter(LootParameters.ORIGIN, livingEntity.getPositionVec()).withParameter(LootParameters.DAMAGE_SOURCE, damageSourceIn).withNullableParameter(LootParameters.KILLER_ENTITY, damageSourceIn.getTrueSource()).withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, damageSourceIn.getImmediateSource());
        if (attackedRecently && playerEntity != null) {
            lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, playerEntity).withLuck(playerEntity.getLuck());
        }
        return lootcontext$builder;
    }
}