package net.shawdy.themostcompletefoodmod;

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
import net.shawdy.themostcompletefoodmod.init.*;
import net.shawdy.themostcompletefoodmod.config.CommonModConfig;
import net.shawdy.themostcompletefoodmod.network.NetworkHandler;
import org.slf4j.Logger;


@Mod(TheMostCompleteFoodMod.MOD_ID)
public class TheMostCompleteFoodMod {

    public static final String MOD_ID = "tmcfm";

    private static final Logger LOGGER = LogUtils.getLogger();

    public TheMostCompleteFoodMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonModConfig.SPEC, "tmcfm-common.toml");
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
