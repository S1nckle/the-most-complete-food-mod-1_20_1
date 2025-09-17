package net.shawdy.alacarte.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.config.CommonModConfig;
import net.shawdy.alacarte.network.C2SRequestDietContainerDataPacket;
import net.shawdy.alacarte.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class DietContainerScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.alacarte.diet_container_screen.title");
    private static final ResourceLocation TEXTURE = new ResourceLocation(ALaCarte.MOD_ID, "textures/gui/player_diet_container_gui.png");
    private Byte protein_fullness, fats_fullness, carbohydrates_fullness, fiber_fullness, minerals_fullness, vitamins_fullness, water_fullness;
    private final static ItemStack potion = new ItemStack(Items.POTION);
    private int imageWidth, imageHeight;
    private int leftPos, topPos;
    private int tickCount;
    private boolean allow_positives = CommonModConfig.ALLOW_POSITIVE.get();
    private boolean allow_negatives = CommonModConfig.ALLOW_NEGATIVE.get();

    public DietContainerScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 182;
        this.imageHeight = 166;
        this.leftPos = (width - imageWidth) / 2;
        this.topPos = (height - imageHeight) / 2;

        this.protein_fullness = 0;
        this.fats_fullness = 0;
        this.carbohydrates_fullness = 0;
        this.fiber_fullness = 0;
        this.minerals_fullness = 0;
        this.vitamins_fullness = 0;
        this.water_fullness = 0;
        this.tickCount = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 10 == 0) {
            NetworkHandler.sendToServer(new C2SRequestDietContainerDataPacket());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawString(this.font, TITLE, leftPos + 5, topPos + 5, 0x404040, false);

        renderStrings(pGuiGraphics);

        renderItems(pGuiGraphics);

        renderDietBars(pGuiGraphics);

        renderToolTips(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }

    private void renderStrings(GuiGraphics pGuiGraphics) {
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.proteins"), leftPos + 5, topPos + 26, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.fats"), leftPos + 92, topPos + 26, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.carbohydrates"), leftPos + 5, topPos + 61, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.fiber"), leftPos + 92, topPos + 61, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.minerals"), leftPos + 5, topPos + 97, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.vitamin"), leftPos + 92, topPos + 97, 0x404040, false);
        pGuiGraphics.drawString(this.font, Component.translatable("gui.alacarte.diet_container_screen.water"), leftPos + 48, topPos + 133, 0x404040, false);
    }

    private void renderItems(GuiGraphics pGuiGraphics) {
        pGuiGraphics.renderItem(new ItemStack(Items.BEEF), this.leftPos + 74, this.topPos + 29);
        pGuiGraphics.renderItem(new ItemStack(Items.MILK_BUCKET), this.leftPos + 161, this.topPos + 29);
        pGuiGraphics.renderItem(new ItemStack(Items.BAKED_POTATO), this.leftPos + 74, this.topPos + 64);
        pGuiGraphics.renderItem(new ItemStack(Items.CARROT), this.leftPos + 161, this.topPos + 64);
        pGuiGraphics.renderItem(new ItemStack(Items.BONE), this.leftPos + 74, this.topPos + 101);
        pGuiGraphics.renderItem(new ItemStack(Items.APPLE), this.leftPos + 161, this.topPos + 100);
        PotionUtils.setPotion(potion, Potions.WATER);
        pGuiGraphics.renderItem(potion, this.leftPos + 115, this.topPos + 136);
    }

    private void renderDietBars(GuiGraphics pGuiGraphics) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        RenderSystem.setShaderColor(0.86f, 0.20f, 0.20f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.protein_fullness, this.leftPos + 4, this.topPos + 35);

        RenderSystem.setShaderColor(0.94f, 0.73f, 0.21f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.fats_fullness, this.leftPos + 91, this.topPos + 35);

        RenderSystem.setShaderColor(0.78f, 0.73f, 0.01f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.carbohydrates_fullness, this.leftPos + 4, this.topPos + 70);

        RenderSystem.setShaderColor(0.16f, 0.56f, 0.01f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.fiber_fullness, this.leftPos + 91, this.topPos + 70);

        RenderSystem.setShaderColor(0.19f, 0.83f, 0.91f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.minerals_fullness, this.leftPos + 4, this.topPos + 106);

        RenderSystem.setShaderColor(0.76f, 0.54f, 0.98f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.vitamins_fullness, this.leftPos + 91, this.topPos + 106);

        RenderSystem.setShaderColor(0.09f, 0.24f, 0.76f, 1.0f);
        RenderSystem.setShaderTexture(2, TEXTURE);
        drawDietBar(pGuiGraphics, this.water_fullness, this.leftPos + 47, this.topPos + 142);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderToolTips(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        List<Component> tooltip = new ArrayList<>();
        boolean flag = Minecraft.getInstance().level.getDifficulty() != Difficulty.PEACEFUL;
        if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 4, this.topPos + 35)) {
            tooltip.add(Component.literal(this.protein_fullness.toString() + '%').withStyle(ChatFormatting.RED));
            if (this.protein_fullness < 5 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_proteins").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.protein_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_proteins").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 91, this.topPos + 35)) {
            tooltip.add(Component.literal(this.fats_fullness.toString() + '%').withStyle(ChatFormatting.GOLD));
            if (this.fats_fullness < 15 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_fats").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.fats_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_fats").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 4, this.topPos + 70)) {
            tooltip.add(Component.literal(this.carbohydrates_fullness.toString() + '%').withStyle(ChatFormatting.YELLOW));
            if (this.carbohydrates_fullness < 15 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_carbohydrates").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.carbohydrates_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_carbohydrates").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 91, this.topPos + 70)) {
            tooltip.add(Component.literal(this.fiber_fullness.toString() + '%').withStyle(ChatFormatting.GREEN));
            if (this.fiber_fullness < 10 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_fiber").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.fiber_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_fiber").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 4, this.topPos + 106)) {
            tooltip.add(Component.literal(this.minerals_fullness.toString() + '%').withStyle(ChatFormatting.AQUA));
            if (this.minerals_fullness < 5 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_minerals").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.minerals_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_minerals").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 91, this.topPos + 106)) {
            tooltip.add(Component.literal(this.vitamins_fullness.toString() + '%').withStyle(ChatFormatting.LIGHT_PURPLE));
            if (this.vitamins_fullness < 5 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_vitamins").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.vitamins_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_vitamins").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else if (isMouseOverProgressBar(pMouseX, pMouseY, this.leftPos + 47, this.topPos + 142)) {
            tooltip.add(Component.literal(this.water_fullness.toString() + '%').withStyle(ChatFormatting.BLUE));
            if (this.water_fullness < 5 && flag && allow_negatives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.low_water").withStyle(ChatFormatting.DARK_GRAY));
            } else if (this.water_fullness > 90 && allow_positives) {
                tooltip.add(Component.translatable("gui.alacarte.diet_container_screen.high_water").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else return;
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, Optional.empty(), pMouseX, pMouseY);
    }

    public void updateDietValues(byte[] values) {
        this.protein_fullness = values[0];
        this.fats_fullness = values[1];
        this.carbohydrates_fullness = values[2];
        this.fiber_fullness = values[3];
        this.minerals_fullness = values[4];
        this.vitamins_fullness = values[5];
        this.water_fullness = values[6];
    }

    private boolean isMouseOverProgressBar(int pMouseX, int pMouseY, int pX, int pY) {
        return pMouseX >= pX && pMouseX <= pX + 71 && pMouseY >= pY && pMouseY <= pY + 6;
    }

    private void drawDietBar(GuiGraphics pGuiGraphics, int fullness, int pX, int pY) {
        pGuiGraphics.blit(TEXTURE, pX, pY, 0, 168, fullness * 71 / 100, 6, 256, 256);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(pKeyCode, pScanCode)) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
