package net.shawdy.themostcompletefoodmod.client.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.CookingPotBlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.TeaPotBlockEntity;
import net.shawdy.themostcompletefoodmod.init.ModBlocks;
import net.shawdy.themostcompletefoodmod.init.ModMenuTypes;
import org.jetbrains.annotations.Nullable;

public class TeaPotBlockEntityMenu extends AbstractContainerMenu {
    private final BlockEntity blockEntity;
    private final SimpleContainerData containerData;
    private final Level level;

    public TeaPotBlockEntityMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pBuffer.readBlockPos()), new SimpleContainerData(2));
    }

    public TeaPotBlockEntityMenu(int pContainerId, Inventory pInventory, BlockEntity blockEntity, SimpleContainerData pData) {
        super(ModMenuTypes.TEAPOT_MENU.get(), pContainerId);

        this.blockEntity = ((TeaPotBlockEntity) blockEntity);
        containerData = pData;
        level = pInventory.player.level();

        createPlayerHotbar(pInventory);
        createPlayerInventory(pInventory);
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

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, ModBlocks.COOKING_POT.get());
    }
}
