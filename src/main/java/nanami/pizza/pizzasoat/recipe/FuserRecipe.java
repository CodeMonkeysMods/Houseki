package nanami.pizza.pizzasoat.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record FuserRecipe(Ingredient inputItem, Ingredient inputItem2, ItemStack output) implements Recipe<FuserRecipeInput> {
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        list.add(this.inputItem2);
        return list;
    }

    @Override
    public boolean matches(FuserRecipeInput input, World world) {
        if (world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0)) && inputItem2.test(input.getStackInSlot(1));
    }

    @Override
    public ItemStack craft(FuserRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FUSER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FUSER_TYPE;
    }

    public static class Serializer implements RecipeSerializer<FuserRecipe> {
        public static final MapCodec<FuserRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(FuserRecipe::inputItem),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient2").forGetter(FuserRecipe::inputItem2),
                ItemStack.CODEC.fieldOf("result").forGetter(FuserRecipe::output)
        ).apply(inst, FuserRecipe::new));
        public static final PacketCodec<RegistryByteBuf, FuserRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, FuserRecipe::inputItem,
                        Ingredient.PACKET_CODEC, FuserRecipe::inputItem2,
                        ItemStack.PACKET_CODEC, FuserRecipe::output,
                        FuserRecipe::new);


        @Override
        public MapCodec<FuserRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FuserRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}