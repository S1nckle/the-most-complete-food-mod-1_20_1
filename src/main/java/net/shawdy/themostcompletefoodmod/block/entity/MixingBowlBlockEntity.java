package net.shawdy.themostcompletefoodmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.themostcompletefoodmod.client.menus.MixingBowlBlockEntityMenu;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MixingBowlBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            MixingBowlBlockEntity.this.setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };
    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> inventory);

    public MixingBowlBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MIXING_BOWL_BE.get(), pPos, pBlockState);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public LazyOptional<ItemStackHandler> getInventoryCapability() {
        return inventoryCapability;
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
        super.saveAdditional(pTag);
        pTag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.tmcfm.mixing_bowl_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MixingBowlBlockEntityMenu(pContainerId, pPlayerInventory, this);
    }
}
