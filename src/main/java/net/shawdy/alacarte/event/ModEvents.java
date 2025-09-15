package net.shawdy.alacarte.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.client.ModKeyMappings;
import net.shawdy.alacarte.client.renderers.CuttingBoardBlockRenderer;
import net.shawdy.alacarte.client.renderers.FryingPanBlockRenderer;
import net.shawdy.alacarte.client.screen.CookingPotBlockEntityMenuScreen;
import net.shawdy.alacarte.client.screen.MixingBowlBLockEntityMenuScreen;
import net.shawdy.alacarte.client.screen.TeapotBlockEntityMenuScreen;
import net.shawdy.alacarte.diet.FoodDietValuesManager;
import net.shawdy.alacarte.init.ModBlockEntities;
import net.shawdy.alacarte.init.ModMenuTypes;
import net.shawdy.alacarte.network.NetworkHandler;

@Mod.EventBusSubscriber(
        modid = ALaCarte.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEvents {

    @SubscribeEvent
    public static void commonSetupEvent(FMLCommonSetupEvent pEvent) {
        pEvent.enqueueWork(() -> {
            NetworkHandler.register();
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent pEvent) {
        ModKeyMappings.register(pEvent);
    }

    @SubscribeEvent
    public static void onClientDataLoad(RegisterClientReloadListenersEvent pEvent) {
        pEvent.registerReloadListener((ResourceManagerReloadListener) manager -> {
            FoodDietValuesManager.load(manager);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent pEvent) {
        // register renderers
        BlockEntityRenderers.register(ModBlockEntities.FRYING_PAN_BE.get(), FryingPanBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.CUTTING_BOARD_BE.get(), CuttingBoardBlockRenderer::new);

        // register menus
        MenuScreens.register(ModMenuTypes.COOKING_POT_MENU.get(), CookingPotBlockEntityMenuScreen::new);
        MenuScreens.register(ModMenuTypes.TEAPOT_MENU.get(), TeapotBlockEntityMenuScreen::new);
        MenuScreens.register(ModMenuTypes.MIXING_BOWL_MENU.get(), MixingBowlBLockEntityMenuScreen::new);
    }

}
