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
import net.shawdy.themostcompletefoodmod.block.entity.TeapotBlockEntity;
import net.shawdy.themostcompletefoodmod.client.menus.TeapotBlockEntityMenu;

public class TeapotBlockEntityMenuScreen extends AbstractContainerScreen<TeapotBlockEntityMenu> {
    private int imageWidth, imageHeight;
    private int leftPos, topPos;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TheMostCompleteFoodMod.MOD_ID, "textures/gui/cooking_pot_block_container.png");
    private final TeapotBlockEntity blockEntity;

    public TeapotBlockEntityMenuScreen(TeapotBlockEntityMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.blockEntity = ((TeapotBlockEntity) pMenu.blockEntity);
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
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
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
