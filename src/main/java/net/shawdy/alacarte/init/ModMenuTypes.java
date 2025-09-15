package net.shawdy.alacarte.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.client.menus.CookingPotBlockEntityMenu;
import net.shawdy.alacarte.client.menus.MixingBowlBlockEntityMenu;
import net.shawdy.alacarte.client.menus.TeapotBlockEntityMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ALaCarte.MOD_ID);

    public static final RegistryObject<MenuType<CookingPotBlockEntityMenu>> COOKING_POT_MENU =
            MENUS.register("cooking_pot_block_menu", () -> IForgeMenuType.create(CookingPotBlockEntityMenu::new));
    public static final RegistryObject<MenuType<TeapotBlockEntityMenu>> TEAPOT_MENU =
            MENUS.register("teapot_block_menu", () -> IForgeMenuType.create(TeapotBlockEntityMenu::new));
    public static final RegistryObject<MenuType<MixingBowlBlockEntityMenu>> MIXING_BOWL_MENU =
            MENUS.register("mixing_bowl_block_menu", () -> IForgeMenuType.create(MixingBowlBlockEntityMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
