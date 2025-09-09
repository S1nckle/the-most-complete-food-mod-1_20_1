package net.shawdy.themostcompletefoodmod.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.themostcompletefoodmod.TheMostCompleteFoodMod;
import net.shawdy.themostcompletefoodmod.client.menus.CookingPotBlockEntityMenu;
import net.shawdy.themostcompletefoodmod.client.menus.TeaPotBlockEntityMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TheMostCompleteFoodMod.MOD_ID);

    public static final RegistryObject<MenuType<CookingPotBlockEntityMenu>> COOKING_POT_MENU =
            MENUS.register("cooking_pot_block_menu", () -> IForgeMenuType.create(CookingPotBlockEntityMenu::new));
    public static final RegistryObject<MenuType<TeaPotBlockEntityMenu>> TEAPOT_MENU =
            MENUS.register("teapot_block_menu", () -> IForgeMenuType.create(TeaPotBlockEntityMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
