package net.shawdy.themostcompletefoodmod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.shawdy.themostcompletefoodmod.TheMostCompleteFoodMod;
import net.shawdy.themostcompletefoodmod.block.entity.CookingPotBlockEntity;
import net.shawdy.themostcompletefoodmod.client.menus.CookingPotBlockEntityMenu;

public class CookingPotBlockEntityMenuScreen extends AbstractContainerScreen<CookingPotBlockEntityMenu> {
    private int imageWidth, imageHeight;
    private int leftPos, topPos;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheMostCompleteFoodMod.MOD_ID, "textures/gui/cooking_pot_block_container.png");
    private final CookingPotBlockEntity blockEntity;

    public CookingPotBlockEntityMenuScreen(CookingPotBlockEntityMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        blockEntity = pMenu.be;
    }

    @Override
    protected void init() {
        super.init();

//        this.titleLabelY = 10000;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.leftPos = (width - imageWidth) / 2;
        this.topPos = (height - imageHeight) / 2;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderIcons(pGuiGraphics);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderIcons(GuiGraphics pGuiGraphics) {
        if (blockEntity.isHeat()) {
              pGuiGraphics.blit(TEXTURE, this.leftPos + 47, this.topPos + 54, 176, 0, 14,12);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }
}
