package net.shawdy.alacarte.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.shawdy.alacarte.ALaCarte;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(
        modid = ALaCarte.MOD_ID,
        value = Dist.CLIENT
)
public class DietContainerButton extends Button {
    private final ResourceLocation ICON;
    final int iconWidth;
    final int iconHeight;

    public DietContainerButton(ResourceLocation pIcon, int x, int y, int width, int height, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.ICON = pIcon;
        this.iconWidth = width;
        this.iconHeight = height;
    }

    @Override
    public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        int iconX = this.getX() + (this.width - this.iconWidth) / 2;
        int iconY = this.getY() + (this.height - this.iconHeight) / 2;
        if (this.isHovered) {
            gui.blit(ICON, iconX, iconY, 0, 16, iconWidth, iconHeight, 256, 256);
        } else {
            gui.blit(ICON, iconX, iconY, 0, 0, iconWidth, iconHeight, 256, 256);
        }
    }
}
