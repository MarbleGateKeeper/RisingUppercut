package love.marblegate.risinguppercut.datagen;

import love.marblegate.risinguppercut.registry.ItemRegistry;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class CraftingRecipesProvider extends ForgeRecipeProvider {
    public CraftingRecipesProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ItemRegistry.GAUNTLET.get())
                .patternLine("ABA")
                .patternLine("ACA")
                .patternLine("DED")
                .key('A',Items.IRON_BLOCK)
                .key('B',ItemRegistry.GAUNTLET_SURFACE.get())
                .key('C',ItemRegistry.GAUNTLET_KINETIC_CORE.get())
                .key('D',ItemRegistry.GAUNTLET_COORDINATE_DRIVE.get())
                .key('E',ItemRegistry.GAUNTLET_COOLER.get())
                .addCriterion("gauntlet_kinetic_core", InventoryChangeTrigger.Instance.forItems(ItemRegistry.GAUNTLET_KINETIC_CORE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ItemRegistry.GAUNTLET_SURFACE.get())
                .patternLine("AAA")
                .patternLine("BCB")
                .patternLine("BCB")
                .key('A',Items.DIAMOND)
                .key('B',Items.IRON_BLOCK)
                .key('C',Items.CHAIN)
                .addCriterion("diamond", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ItemRegistry.GAUNTLET_KINETIC_CORE.get())
                .patternLine("AAA")
                .patternLine("BCB")
                .patternLine("DED")
                .key('A',Items.IRON_BLOCK)
                .key('B',Items.PISTON)
                .key('C',Items.SLIME_BLOCK)
                .key('D',Items.REDSTONE_BLOCK)
                .key('E',Items.STICKY_PISTON)
                .addCriterion("piston", InventoryChangeTrigger.Instance.forItems(Items.PISTON))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ItemRegistry.GAUNTLET_COORDINATE_DRIVE.get())
                .patternLine("ABA")
                .patternLine("B B")
                .patternLine("ABA")
                .key('A',Items.REDSTONE_BLOCK)
                .key('B',Items.ENDER_EYE)
                .addCriterion("ender_eye", InventoryChangeTrigger.Instance.forItems(Items.ENDER_EYE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ItemRegistry.GAUNTLET_COOLER.get())
                .patternLine("A A")
                .patternLine("ABA")
                .patternLine("ACA")
                .key('A',Items.IRON_BLOCK)
                .key('B',Ingredient.fromItems(Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE, Items.SNOW_BLOCK))
                .key('C',Items.DISPENSER)
                .addCriterion("ice", InventoryChangeTrigger.Instance.forItems(Items.ICE))
                .build(consumer);

    }
}
