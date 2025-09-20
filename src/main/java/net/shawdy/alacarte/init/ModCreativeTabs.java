package net.shawdy.alacarte.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.alacarte.ALaCarte;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ALaCarte.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALACARTE_EDIBLES = CREATIVE_MOD_TABS.register("alacarte_edibles",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.COOKED_BEEF))
                    .title(Component.translatable("creativetab.alacarte.edibles"))
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.CARAMEL_APPLE.get());
                    })).build());

    public static final RegistryObject<CreativeModeTab> ALACARTE_DRINKS = CREATIVE_MOD_TABS.register("alacarte_drinks", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.LEATHER_FLASK.get()))
                    .title(Component.translatable("creativetab.alacarte.drinks"))
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.WATER_BOWL.get());
                        pOutput.accept(ModItems.LEATHER_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.LEATHER_FLASK.get());
                        pOutput.accept(ModItems.COPPER_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.COPPER_FLASK.get());
                        pOutput.accept(ModItems.IRON_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.IRON_FLASK.get());
                        pOutput.accept(ModItems.GOLDEN_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.GOLDEN_FLASK.get());
                        pOutput.accept(ModItems.DIAMOND_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.DIAMOND_FLASK.get());
                        pOutput.accept(ModItems.NETHERITE_FLASK.get().getDefaultInstance());
                        pOutput.accept(ModItems.NETHERITE_FLASK.get());
                        pOutput.accept(ModItems.CUP_ITEM.get());
                        pOutput.accept(ModItems.MUG_ITEM.get());
                        pOutput.accept(ModItems.BEER_MUG_ITEM.get());
                    })).build());

    public static final RegistryObject<CreativeModeTab> ALACARTE_BLOCKS = CREATIVE_MOD_TABS.register("alacarte_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COOKING_POT_ITEM.get()))
                    .title(Component.translatable("creativetab.alacarte.blocks"))
                    .displayItems((((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.MIXING_BOWL.get());
                        pOutput.accept(ModItems.COOKING_POT_ITEM.get());
                        pOutput.accept(ModItems.FRYING_PAN_ITEM.get());
                        pOutput.accept(ModItems.TEAPOT_ITEM.get());
                        pOutput.accept(ModBlocks.CUTTING_BOARD.get());
                        pOutput.accept(ModBlocks.FERMENTATION_POT.get());
                    }))).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
