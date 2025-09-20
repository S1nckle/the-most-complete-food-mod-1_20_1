package net.shawdy.alacarte.client.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.shawdy.alacarte.block.entity.TeapotBlockEntity;
import net.shawdy.alacarte.init.ModBlocks;
import net.shawdy.alacarte.init.ModItems;
import net.shawdy.alacarte.init.ModMenuTypes;
import org.jetbrains.annotations.NotNull;

public class TeapotBlockEntityMenu extends AbstractContainerMenu {
    public final BlockEntity blockEntity;
    private final ContainerData containerData;
    private final Level level;

    public TeapotBlockEntityMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pBuffer.readBlockPos()), new SimpleContainerData(2));
    }

    public TeapotBlockEntityMenu(int pContainerId, Inventory pInventory, BlockEntity blockEntity, ContainerData pData) {
        super(ModMenuTypes.TEAPOT_MENU.get(), pContainerId);

        this.blockEntity = ((TeapotBlockEntity) blockEntity);
        containerData = pData;
        level = pInventory.player.level();

        createPlayerHotbar(pInventory);
        createPlayerInventory(pInventory);
        createBlockInventory((TeapotBlockEntity) blockEntity);
        addDataSlots(containerData);
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

    private void createBlockInventory(TeapotBlockEntity be) {
        be.getInventoryCapability().ifPresent(inventory -> {
            int id = 0;
            addSlot(new SlotItemHandler(inventory, id++, 37, 17));
            addSlot(new SlotItemHandler(inventory, id++, 55, 17));
            addSlot(new SlotItemHandler(inventory, id++, 37, 35));
            addSlot(new SlotItemHandler(inventory, id++, 55, 35));

            addSlot(new SlotItemHandler(inventory, id++, 123, 25) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player playerIn) {
                    return false;
                }
            });
            addSlot(new SlotItemHandler(inventory, id++, 91, 54) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return TeapotBlockEntityMenu.this.isCup(stack);
                }
            });
            addSlot(new SlotItemHandler(inventory, id++, 123, 54) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });
    }

    private boolean isCup(@NotNull ItemStack stack) {
        return stack.is(ModItems.CUP_ITEM.get()) || stack.is(ModItems.MUG_ITEM.get());
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        ItemStack sourceStack = sourceSlot.getItem();
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (pIndex < 36) {
            if (isCup(sourceStack)) {
                moveItemStackTo(sourceStack, 42, 43, false);
            }
            if (!moveItemStackTo(sourceStack, 36, 40, false)) {
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.TEAPOT.get());
    }
}
