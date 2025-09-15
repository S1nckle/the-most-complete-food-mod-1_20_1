package net.shawdy.alacarte.client.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import net.shawdy.alacarte.block.entity.MixingBowlBlockEntity;
import net.shawdy.alacarte.init.ModBlocks;
import net.shawdy.alacarte.init.ModMenuTypes;
import org.jetbrains.annotations.NotNull;

public class MixingBowlBlockEntityMenu extends AbstractContainerMenu {
    private final Level pLevel;
    private final MixingBowlBlockEntity pBlockEntity;
    private ContainerLevelAccess access;
    public MixingBowlBlockEntityMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, ((MixingBowlBlockEntity) pInventory.player.level().getBlockEntity(pBuffer.readBlockPos())));
    }

    public MixingBowlBlockEntityMenu(int pContainerId, Inventory pInventory, MixingBowlBlockEntity pBlockEntity) {
        super(ModMenuTypes.MIXING_BOWL_MENU.get(), pContainerId);
        pLevel = pInventory.player.level();
        this.pBlockEntity = pBlockEntity;
        access = ContainerLevelAccess.create(pLevel, pBlockEntity.getBlockPos());

        createPlayerHotbar(pInventory);
        createPlayerInventory(pInventory);
        createBlockInventory(pBlockEntity);
    }

    private void createBlockInventory(MixingBowlBlockEntity be) {
        pBlockEntity.getInventoryCapability().ifPresent(inventory -> {
            int id = 0;
            addSlot(new SlotItemHandler(inventory, id++, 30, 36));
            addSlot(new SlotItemHandler(inventory, id++, 48, 36));
            addSlot(new SlotItemHandler(inventory, id++, 66, 36));
            addSlot(new SlotItemHandler(inventory, id++, 30, 54));
            addSlot(new SlotItemHandler(inventory, id++, 48, 54));
            addSlot(new SlotItemHandler(inventory, id++, 66, 54));

            addSlot(new SlotItemHandler(inventory, id++, 57, 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return isMixingInstrument(stack);
                }
            });
            addSlot(new SlotItemHandler(inventory, id++, 124, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });
    }

    private boolean isMixingInstrument(ItemStack stack) {
        return false;
    }

    private void createPlayerInventory(Inventory playerInv) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row*9), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column*18), 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        ItemStack sourceStack = sourceSlot.getItem();
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (pIndex < 36) {
            if (isMixingInstrument(sourceStack)) {
                moveItemStackTo(sourceStack, 42, 43, false);
            }
            if (!moveItemStackTo(sourceStack, 36, 43, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < 43) {
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(pPlayer, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(access, pPlayer, ModBlocks.MIXING_BOWL.get());
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        for (int i = 36; i < 42; i++) {
            Slot slot = getSlot(i);
            if (slot.hasItem()) {
                pPlayer.getInventory().placeItemBackInInventory(slot.getItem());
                slot.set(ItemStack.EMPTY);
            }
        }
    }
}
