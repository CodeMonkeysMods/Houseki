package nanami.pizza.pizzasoat.recipe;

import nanami.pizza.pizzasoat.PizzasOAT;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<FuserRecipe> FUSER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(PizzasOAT.MOD_ID, "fusing"), new FuserRecipe.Serializer());
    public static final RecipeType<FuserRecipe> FUSER_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(PizzasOAT.MOD_ID, "fusing"), new RecipeType<>() {
                @Override
                public String toString() {
                    return "fusing";
                }
            });



    public static void registerRecipes() {
        PizzasOAT.LOGGER.info("Registering Custom Recipes for " + PizzasOAT.MOD_ID);
    }
}