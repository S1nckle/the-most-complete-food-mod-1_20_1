package net.shawdy.themostcompletefoodmod.event;

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
import net.shawdy.themostcompletefoodmod.TheMostCompleteFoodMod;
import net.shawdy.themostcompletefoodmod.block.custom.CookingPotBlock;
import net.shawdy.themostcompletefoodmod.client.ModKeyMappings;
import net.shawdy.themostcompletefoodmod.client.renderers.CuttingBoardBlockRenderer;
import net.shawdy.themostcompletefoodmod.client.renderers.FryingPanBlockRenderer;
import net.shawdy.themostcompletefoodmod.client.screen.CookingPotBlockEntityMenuScreen;
import net.shawdy.themostcompletefoodmod.diet.FoodDietValuesManager;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;
import net.shawdy.themostcompletefoodmod.init.ModMenuTypes;
import net.shawdy.themostcompletefoodmod.network.NetworkHandler;

@Mod.EventBusSubscriber(
        modid = TheMostCompleteFoodMod.MOD_ID,
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
    }

}
