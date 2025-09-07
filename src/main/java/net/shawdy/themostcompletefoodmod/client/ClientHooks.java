package net.shawdy.themostcompletefoodmod.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.shawdy.themostcompletefoodmod.client.screen.DietContainerScreen;

@OnlyIn(Dist.CLIENT)
public class ClientHooks {
    public static void setPlayerDietContainerScreen() {
        Minecraft.getInstance().setScreen(new DietContainerScreen());
    }
}

