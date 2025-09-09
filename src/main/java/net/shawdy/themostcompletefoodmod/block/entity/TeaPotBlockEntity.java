package net.shawdy.themostcompletefoodmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;

public class TeaPotBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            TeaPotBlockEntity.this.setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };
    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> inventory);
    private int cookingTime, cookingProgress;
    private final ContainerData containerData;

    public TeaPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TEAPOT_BE.get(), pPos, pBlockState);
        containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> cookingProgress;
                    case 1 -> cookingTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        cookingProgress = pValue;
                    case 1:
                        cookingTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public LazyOptional<ItemStackHandler> getInventoryCapability() {
        return inventoryCapability;
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
}
