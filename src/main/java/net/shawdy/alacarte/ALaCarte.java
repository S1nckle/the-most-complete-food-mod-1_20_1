package net.shawdy.alacarte;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.shawdy.alacarte.init.*;
import net.shawdy.alacarte.config.CommonModConfig;
import net.shawdy.alacarte.network.NetworkHandler;
import org.slf4j.Logger;


@Mod(ALaCarte.MOD_ID)
public class ALaCarte {

    public static final String MOD_ID = "alacarte";

    private static final Logger LOGGER = LogUtils.getLogger();

    public ALaCarte() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonModConfig.SPEC, "alacarte-common.toml");
    }

    private void commonSetup(FMLCommonSetupEvent pEvent) {
        pEvent.enqueueWork(NetworkHandler::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
