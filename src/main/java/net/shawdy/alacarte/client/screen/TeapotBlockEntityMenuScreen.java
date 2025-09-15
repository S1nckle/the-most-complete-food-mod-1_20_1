package net.shawdy.alacarte.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.block.entity.TeapotBlockEntity;
import net.shawdy.alacarte.client.menus.TeapotBlockEntityMenu;

public class TeapotBlockEntityMenuScreen extends AbstractContainerScreen<TeapotBlockEntityMenu> {
    private int imageWidth, imageHeight;
    private int leftPos, topPos;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ALaCarte.MOD_ID, "textures/gui/teapot_block_container.png");
    private final TeapotBlockEntity blockEntity;

    public TeapotBlockEntityMenuScreen(TeapotBlockEntityMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.blockEntity = ((TeapotBlockEntity) pMenu.blockEntity);
    }

    @Override
    protected void init() {
        super.init();

//        this.titleLabelY = 10000;
        this.titleLabelX = leftPos + 110;
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
    protected void renderBg(GuiGraphics pGuiGraphics, float v, int i, int i1) {
        super.renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }
}
