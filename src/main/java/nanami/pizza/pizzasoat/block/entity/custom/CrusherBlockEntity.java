package nanami.pizza.pizzasoat.block.entity.custom;

import nanami.pizza.pizzasoat.block.custom.CrusherBlock;
import nanami.pizza.pizzasoat.block.entity.ImplementedInventory;
import nanami.pizza.pizzasoat.block.entity.ModBlockEntities;
import nanami.pizza.pizzasoat.item.ModItems;
import nanami.pizza.pizzasoat.recipe.CrusherRecipe;
import nanami.pizza.pizzasoat.recipe.CrusherRecipeInput;
import nanami.pizza.pizzasoat.recipe.ModRecipes;
import nanami.pizza.pizzasoat.screen.custom.CrusherScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrusherBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = CrusherRecipe.DEFAULT_CRUSHING_TIME;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    private int lastValidFuelTime = 0;
    private boolean wasCrafting = false;

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHER_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CrusherBlockEntity.this.progress;
                    case 1 -> CrusherBlockEntity.this.maxProgress;
                    case 2 -> CrusherBlockEntity.this.getStableFuelTime();
                    case 3 -> CrusherBlockEntity.this.maxFuelTime;
                    case 4 -> CrusherBlockEntity.this.wasCrafting ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CrusherBlockEntity.this.progress = value;
                    case 1 -> CrusherBlockEntity.this.maxProgress = value;
                    case 2 -> CrusherBlockEntity.this.fuelTime = value;
                    case 3 -> CrusherBlockEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }

    private int getFuelTime(ItemStack fuel) {
        if (fuel.getItem() == Items.IRON_INGOT) {
            return 1600; //How long a single ingots should "burn" in ticks
        }
        return 0;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.pizzasoat.crusher");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrusherScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("crusher.progress", progress);
        nbt.putInt("crusher.max_progress", maxProgress);
        nbt.putInt("crusher.fuel_time", fuelTime);
        nbt.putInt("crusher.max_fuel_time", maxFuelTime);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("crusher.progress");
        maxProgress = nbt.getInt("crusher.max_progress");
        fuelTime = nbt.getInt("crusher.fuel_time");
        maxFuelTime = nbt.getInt("crusher.max_fuel_time");
        super.readNbt(nbt, registryLookup);
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        Optional<RecipeEntry<CrusherRecipe>> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            maxProgress = recipe.get().value().crushingTime();
        } else {
            maxProgress = CrusherRecipe.DEFAULT_CRUSHING_TIME;
        }

        boolean isCurrentlyCrafting = fuelTime > 0 && hasRecipe() && canInsertIntoOutputSlot();
        wasCrafting = isCurrentlyCrafting || (progress > 0);

        if (fuelTime > 0) {
            fuelTime--;
        }

        if (fuelTime == 0 && canCraft()) {
            ItemStack fuelStack = this.getStack(FUEL_SLOT);
            int fuelVal = getFuelTime(fuelStack);
            if (fuelVal > 0) {
                fuelTime = fuelVal;
                maxFuelTime = fuelVal;
                fuelStack.decrement(1);
                markDirty();
            }
        }

        boolean canCraftNow = fuelTime > 0 && hasRecipe() && canInsertIntoOutputSlot();

        if(canCraftNow) {
            increaseCraftingProgress();
            world.setBlockState(pos, state.with(CrusherBlock.LIT, true));

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            world.setBlockState(pos, state.with(CrusherBlock.LIT, false));
            if (fuelTime <= 0) {
                resetProgress();
            }
        }

        markDirty(world, pos, state);
    }

    public int getStableFuelTime() {
        if (fuelTime > 0) {
            lastValidFuelTime = fuelTime;
            return fuelTime;
        }

        if (hasRecipe() && canInsertIntoOutputSlot()) {
            return lastValidFuelTime;
        }

        return 0;
    }

    private boolean canCraft() {
        return hasRecipe() && canInsertIntoOutputSlot();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Optional<RecipeEntry<CrusherRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        this.removeStack(INPUT_SLOT, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(recipe.get().value().output().getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + recipe.get().value().output().getCount()));
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean canInsertIntoOutputSlot() {
        return this.getStack(OUTPUT_SLOT).isEmpty() ||
                this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<CrusherRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().getResult(null);
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeEntry<CrusherRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager().getFirstMatch(ModRecipes.CRUSHER_TYPE, new CrusherRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());

    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.getStack(OUTPUT_SLOT).getMaxCount() >= this.getStack(OUTPUT_SLOT).getCount() + count;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
