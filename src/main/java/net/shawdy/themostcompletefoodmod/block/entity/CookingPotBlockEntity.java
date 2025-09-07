package net.shawdy.themostcompletefoodmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.themostcompletefoodmod.client.menus.CookingPotBlockEntityMenu;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CookingPotBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            CookingPotBlockEntity.this.setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };
    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> inventory);
    protected final ContainerData containerData;
    private int cookingProgress;
    private int cookingTime;

    public CookingPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COOKING_POT_BE.get(), pPos, pBlockState);
        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch(pIndex) {
                    case 0 -> CookingPotBlockEntity.this.cookingProgress;
                    case 1 -> CookingPotBlockEntity.this.cookingTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch(pIndex) {
                    case 0: CookingPotBlockEntity.this.cookingProgress = pValue;
                    case 1: CookingPotBlockEntity.this.cookingTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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
        inventoryCapability.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Inventory", inventory.serializeNBT());
        pTag.putInt("CookingProgress", cookingProgress);
        pTag.putInt("CookingTime", cookingTime);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        inventory.deserializeNBT(pTag.getCompound("Inventory"));
        cookingProgress = pTag.getInt("CookingProgress");
        cookingTime = pTag.getInt("CookingTime");
    }

    public void dropInventory() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(),
                    worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tmcfm.cooking_pot_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CookingPotBlockEntityMenu(pContainerId, pPlayerInventory, this, containerData);
    }

}
