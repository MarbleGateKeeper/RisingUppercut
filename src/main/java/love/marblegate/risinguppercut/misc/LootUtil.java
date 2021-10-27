package love.marblegate.risinguppercut.misc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

//TODO I believe we need something more reliable and predictable
public class LootUtil {
    public static void dropLoot(LivingEntity livingEntity, DamageSource damageSourceIn, boolean attackedRecently, Player player) {
        ResourceLocation resourcelocation = livingEntity.getLootTable();
        LootTable loottable = livingEntity.level.getServer().getLootTables().get(resourcelocation);
        LootContext.Builder lootcontext$builder = getLootContextBuilder(livingEntity, attackedRecently, damageSourceIn, player);
        LootContext ctx = lootcontext$builder.create(LootContextParamSets.ENTITY);
        loottable.getRandomItems(ctx).forEach(livingEntity::spawnAtLocation);
    }

    public static LootContext.Builder getLootContextBuilder(LivingEntity livingEntity, boolean attackedRecently, DamageSource damageSourceIn, Player player) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) livingEntity.level)).withRandom(livingEntity.getRandom()).withParameter(LootContextParams.THIS_ENTITY, livingEntity).withParameter(LootContextParams.ORIGIN, livingEntity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSourceIn).withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSourceIn.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSourceIn.getDirectEntity());
        if (attackedRecently && player != null) {
            lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }
        return lootcontext$builder;
    }
}