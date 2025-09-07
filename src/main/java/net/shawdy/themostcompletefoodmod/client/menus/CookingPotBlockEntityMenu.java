package net.shawdy.themostcompletefoodmod.client.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.CookingPotBlockEntity;
import net.shawdy.themostcompletefoodmod.init.ModBlocks;
import net.shawdy.themostcompletefoodmod.init.ModMenuTypes;

public class CookingPotBlockEntityMenu extends AbstractContainerMenu {
    private final CookingPotBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;

    public CookingPotBlockEntityMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pData) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pData.readBlockPos()));
    }

    public CookingPotBlockEntityMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity pBlockEntity) {
        super(ModMenuTypes.COOKING_POT_MENU.get(), pContainerId);
        Level pLevel = pBlockEntity.getLevel();
        if (pLevel != null && pBlockEntity instanceof CookingPotBlockEntity pot) {
            blockEntity = pot;

            levelAccess = ContainerLevelAccess.create(pot.getLevel(), pot.getBlockPos());

        } else {
            throw (new IllegalStateException());
        }

    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(
                levelAccess,
                pPlayer,
                ModBlocks.COOKING_POT.get()
        );
    }
}
