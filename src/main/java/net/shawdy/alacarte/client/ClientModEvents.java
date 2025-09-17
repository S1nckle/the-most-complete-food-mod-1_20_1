package net.shawdy.alacarte.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.client.screen.DietContainerButton;
import net.shawdy.alacarte.client.screen.DietContainerScreen;
import net.shawdy.alacarte.diet.DietValuesHolder;
import net.shawdy.alacarte.diet.FoodDietValuesManager;
import net.shawdy.alacarte.network.C2SRequestDietContainerDataPacket;
import net.shawdy.alacarte.network.NetworkHandler;

import java.util.List;


@Mod.EventBusSubscriber(
        modid = ALaCarte.MOD_ID,
        value = Dist.CLIENT
)
public class ClientModEvents {
    private static DietContainerButton button;

    @SubscribeEvent
    public static void onInitGui(ScreenEvent.Init.Post pEvent) {
        ResourceLocation ICON = new ResourceLocation(ALaCarte.MOD_ID, "textures/gui/diet_container_button.png");
        if (pEvent.getScreen() instanceof InventoryScreen pInventoryScreen) {
            button = new DietContainerButton(
                    ICON,
                    pInventoryScreen.getGuiLeft() + 77,
                    pInventoryScreen.getGuiTop() + 44,
                    16, 16,
                    btn -> {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHooks::setPlayerDietContainerScreen);
                    });
            pEvent.addListener(button);
        }
        if (pEvent.getScreen() instanceof DietContainerScreen) {
            NetworkHandler.sendToServer(new C2SRequestDietContainerDataPacket());
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post pEvent) {
        if (pEvent.getScreen() instanceof InventoryScreen inventoryScreen) {
            if (button != null) {
                button.setX(inventoryScreen.getGuiLeft() + 77);
            }
        }
    }

    @SubscribeEvent
    public static void onToolTipRender(ItemTooltipEvent pEvent) {
        ItemStack pStack = pEvent.getItemStack();
        ResourceLocation pId = ForgeRegistries.ITEMS.getKey(pStack.getItem());
        DietValuesHolder data = FoodDietValuesManager.getData(pId);

        if (data != null) {
            List<Component> pToolTip = pEvent.getToolTip();
            int insertpos = 1;

            if (data.getProteins() != 0) {
                String modifier = data.getProteins() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getProteins() + " ").withStyle(ChatFormatting.RED)
                        .append(Component.translatable("tooltip.alacarte.diet_values.proteins").withStyle(ChatFormatting.RED)));
            }
            if (data.getFats() != 0) {
                String modifier = data.getFats() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getFats() + " ").withStyle(ChatFormatting.GOLD)
                        .append(Component.translatable("tooltip.alacarte.diet_values.fats").withStyle(ChatFormatting.GOLD)));
            }
            if (data.getCarbohydrates() != 0) {
                String modifier = data.getCarbohydrates() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getCarbohydrates() + " ").withStyle(ChatFormatting.YELLOW)
                        .append(Component.translatable("tooltip.alacarte.diet_values.carbohydrates").withStyle(ChatFormatting.YELLOW)));
            }
            if (data.getFiber() != 0) {
                String modifier = data.getFiber() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getFiber() + " ").withStyle(ChatFormatting.DARK_GREEN)
                        .append(Component.translatable("tooltip.alacarte.diet_values.fiber").withStyle(ChatFormatting.DARK_GREEN)));
            }
            if (data.getMinerals() != 0) {
                String modifier = data.getMinerals() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getMinerals() + " ").withStyle(ChatFormatting.AQUA)
                        .append(Component.translatable("tooltip.alacarte.diet_values.minerals").withStyle(ChatFormatting.AQUA)));
            }
            if (data.getVitamins() != 0) {
                String modifier = data.getVitamins() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getVitamins() + " ").withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.translatable("tooltip.alacarte.diet_values.vitamins").withStyle(ChatFormatting.LIGHT_PURPLE)));
            }
            if(data.getWater() != 0) {
                String modifier = data.getWater() > 0 ? "+" : "";
                pToolTip.add(insertpos++, Component.literal(modifier + data.getWater() + " ").withStyle(ChatFormatting.BLUE)
                        .append(Component.translatable("tooltip.alacarte.diet_values.water").withStyle(ChatFormatting.BLUE)));
            }
        }
    }
}

