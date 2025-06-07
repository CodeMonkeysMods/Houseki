package nanami.pizza.pizzasoat;

import nanami.pizza.pizzasoat.block.ModBlocks;
import nanami.pizza.pizzasoat.screen.ModScreenHandlers;
import nanami.pizza.pizzasoat.screen.custom.FuserScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class PizzasOATClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        HandledScreens.register(ModScreenHandlers.FUSER_SCREEN_HANDLER, FuserScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALUMINUM_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALUMINUM_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALUMINUM_TRAPDOOR, RenderLayer.getCutout());
    }
}
