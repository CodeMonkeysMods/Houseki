package nanami.pizza.pizzasoat.block.entity;

import nanami.pizza.pizzasoat.PizzasOAT;
import nanami.pizza.pizzasoat.block.ModBlocks;
import nanami.pizza.pizzasoat.block.entity.custom.FuserBE;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<FuserBE> FUSER_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(PizzasOAT.MOD_ID, "fuser_be"), BlockEntityType.Builder.create(FuserBE::new, ModBlocks.FUSER)
                    .build(null));

    public static void registerBlockEntities() {
        PizzasOAT.LOGGER.info("Registering Block Entities for " + PizzasOAT.MOD_ID);
    }
}