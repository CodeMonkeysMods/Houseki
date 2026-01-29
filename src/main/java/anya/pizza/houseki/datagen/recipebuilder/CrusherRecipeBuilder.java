package anya.pizza.houseki.datagen.recipebuilder;

import anya.pizza.houseki.recipe.CrusherRecipe;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public abstract class CrusherRecipeBuilder implements CraftingRecipeJsonBuilder {
    public final Ingredient input;
    public final ItemStack output;
    public final int crushingTime;
    private static Optional<ItemStack> auxiliaryOutput = Optional.empty();
    private static double auxiliaryChance = 1;
    final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    public String group;

    public CrusherRecipeBuilder(Ingredient input, ItemStack output, int crushingTime) {
        this.input = input;
        this.output = output;
        this.crushingTime = crushingTime;
    }

    public static CrusherRecipeBuilder create(Ingredient input, ItemStack output, int crushingTime) {
        return new CrusherRecipeBuilder(input, output, crushingTime) {
            @Override
            public void offerTo(RecipeExporter exporter, Identifier recipeKey) {
                Advancement.Builder advancement = exporter.getAdvancementBuilder()
                        .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
                        .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                        .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
                this.criteria.forEach(advancement::criterion);

        // Create an instance of your recipe record
        CrusherRecipe recipe = new CrusherRecipe(input, output, crushingTime, auxiliaryOutput, auxiliaryChance);

                // Export it using the built-in exporter
                exporter.accept(recipeKey, recipe, null);

            }
        };
    }

    public CrusherRecipeBuilder auxiliary(ItemStack stack) {
        auxiliaryOutput = Optional.of(stack);
        return this;
    }

    public CrusherRecipeBuilder chance(double chance) {
        auxiliaryChance = chance;
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return Items.AIR;
    }
}