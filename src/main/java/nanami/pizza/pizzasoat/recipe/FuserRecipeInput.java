package nanami.pizza.pizzasoat.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;


public record FuserRecipeInput(ItemStack input, ItemStack input2) implements RecipeInput {
    public FuserRecipeInput(ItemStack input, ItemStack input2) {
        this.input = input;
        this.input2 = input2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack var10000;
        switch (slot) {
            case 0 -> var10000 = this.input;
            case 1 -> var10000 = this.input2;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        }

        return var10000;
    }

    @Override
    public int getSize() {
        return 2;
    }

    public boolean isEmpty() {
        return this.input.isEmpty() && this.input2.isEmpty();
    }

    public ItemStack input() {
        return this.input;
    }

    public ItemStack input2() {
        return this.input2;
    }

}

