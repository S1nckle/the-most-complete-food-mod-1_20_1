package net.shawdy.themostcompletefoodmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FryingPanBlockEntity extends BlockEntity {
    private int cookingProgress;
    private int cookingTime;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> recipeCheck =
            RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            FryingPanBlockEntity.this.setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> inventory);

    public FryingPanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FRYING_PAN_BE.get(), pPos, pBlockState);
    }

    public void cookTick(Level pLevel, BlockPos pPos, BlockState pState, FryingPanBlockEntity panBlockEntity) {
        ItemStack pStack = panBlockEntity.getInventory().getStackInSlot(0);
        if (pStack.getCount() <= 0) {
            panBlockEntity.cookingProgress = 0;
            return;
        }
        panBlockEntity.cookingProgress++;
        if (panBlockEntity.cookingProgress >= panBlockEntity.cookingTime) {
            Container container = new SimpleContainer(pStack);
            ItemStack result = panBlockEntity.recipeCheck.getRecipeFor(container, pLevel).map((recipe) ->
                    recipe.assemble(container, pLevel.registryAccess())).orElse(pStack);
            Containers.dropItemStack(pLevel, (double) pPos.getX(), (double) pPos.getY() + 0.3, (double) pPos.getZ(), result);
            pStack.shrink(1);
            pLevel.sendBlockUpdated(pPos, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pState));
            panBlockEntity.cookingProgress = 0;
        }
    }

    public void waitTick(Level pLevel, BlockPos pPos, BlockState pState, FryingPanBlockEntity panBlockEntity) {
        if (panBlockEntity.cookingProgress != 0) {
            panBlockEntity.cookingProgress = Math.max(0, panBlockEntity.cookingProgress - 2);
            panBlockEntity.setChanged();
        }
    }


    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("CookingProgress", cookingProgress);
        pTag.putInt("CookingTime", cookingTime);
        pTag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Inventory")) {
            inventory.deserializeNBT(pTag.getCompound("Inventory"));
        }
        if (pTag.contains("CookingProgress")) {
            cookingProgress = pTag.getInt("CookingProgress");
        }
        if (pTag.contains("CookingTime")) {
            cookingTime = pTag.getInt("CookingTime");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag pTag = super.getUpdateTag();
        saveAdditional(pTag);
        return pTag;
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCapability.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCapability.invalidate();
    }

    public void dropInventory() {
        ItemStackHandler pInventory = getInventory();

        if (!pInventory.getStackInSlot(0).isEmpty() && level != null) {
            Containers.dropItemStack(level, worldPosition.getX(),
                    worldPosition.getY(), worldPosition.getZ(), pInventory.getStackInSlot(0));
        }
    }

    public Optional<CampfireCookingRecipe> getCookingRecipe(ItemStack pStack) {
        if (level != null && !level.isClientSide()) {
            return pStack.isEmpty() ? Optional.empty() : this.recipeCheck.getRecipeFor(new SimpleContainer(pStack), level);
        }
        return Optional.empty();
    }

    public void placeCookingItem(ItemStack pStack, int pCookingTime) {
        inventory.setStackInSlot(0, pStack);
        this.cookingTime = pCookingTime;
        this.setChanged();
        this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public int getCookingProgress() {
        return this.cookingProgress;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }
}
