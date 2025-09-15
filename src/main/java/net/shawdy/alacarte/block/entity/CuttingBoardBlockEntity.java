package net.shawdy.alacarte.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.shawdy.alacarte.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

public class CuttingBoardBlockEntity extends BlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            CuttingBoardBlockEntity.this.setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    private final LazyOptional<Container> inventoryCapability = LazyOptional.of(() -> inventory);

    public CuttingBoardBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CUTTING_BOARD_BE.get(), pPos, pBlockState);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", inventory.createTag());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Inventory")) {
            inventory.fromTag(pTag.getList("Inventory", 10));
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
        SimpleContainer pInventory = getInventory();

        if (!pInventory.getItem(0).isEmpty() && level != null) {
            Containers.dropItemStack(level, worldPosition.getX(),
                    worldPosition.getY(), worldPosition.getZ(), pInventory.getItem(0));
        }
    }
}
