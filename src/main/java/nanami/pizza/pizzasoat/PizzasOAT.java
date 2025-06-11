package nanami.pizza.pizzasoat;

import nanami.pizza.pizzasoat.block.ModBlocks;
import nanami.pizza.pizzasoat.block.entity.ModBlockEntities;
import nanami.pizza.pizzasoat.item.ModItemGroups;
import nanami.pizza.pizzasoat.item.ModItems;
import nanami.pizza.pizzasoat.screen.ModScreenHandlers;
import nanami.pizza.pizzasoat.world.gen.ModWorldGeneration;
import nanami.pizza.pizzasoat.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzasOAT implements ModInitializer {
	public static final String MOD_ID = "pizzasoat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerItemGroups();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();

		ModWorldGeneration.generateModWorldGeneration();

		FuelRegistry.INSTANCE.add(ModItems.SULFUR, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.BLOCK_OF_SULFUR, 16000);
	}
}