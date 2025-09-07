package net.shawdy.themostcompletefoodmod.client.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.CookingPotBlockEntity;
import net.shawdy.themostcompletefoodmod.init.ModBlocks;
import net.shawdy.themostcompletefoodmod.init.ModMenuTypes;

public class CookingPotBlockEntityMenu extends AbstractContainerMenu {
    public final CookingPotBlockEntity be;
    private final Level level;
    private final ContainerData containerData;

    public CookingPotBlockEntityMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pBuffer) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pBuffer.readBlockPos()), new SimpleContainerData(2));
    }

    public CookingPotBlockEntityMenu(int pContainerid, Inventory pInventory, BlockEntity be, ContainerData pContainerData) {
        super(ModMenuTypes.COOKING_POT_MENU.get(), pContainerid);
        this.be = (CookingPotBlockEntity) be;
        this.level = pInventory.player.level();
        this.containerData = pContainerData;

        createPlayerInventory(pInventory);
        createPlayerHotbar(pInventory);
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
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), pPlayer, ModBlocks.COOKING_POT.get());
    }
}
