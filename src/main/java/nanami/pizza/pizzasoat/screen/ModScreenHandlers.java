package nanami.pizza.pizzasoat.screen;

import nanami.pizza.pizzasoat.PizzasOAT;
import nanami.pizza.pizzasoat.screen.custom.CrusherScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<CrusherScreenHandler> CRUSHER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(PizzasOAT.MOD_ID, "crusher_screen_handler"),
                    new ExtendedScreenHandlerType<>(CrusherScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        PizzasOAT.LOGGER.info("Registering Screen Handlers for " + PizzasOAT.MOD_ID);
    }
}
