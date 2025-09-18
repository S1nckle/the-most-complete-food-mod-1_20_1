package net.shawdy.alacarte.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.item.ModFoodProperties;
import net.shawdy.alacarte.item.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            ALaCarte.MOD_ID);

    //Drinks
    public static final RegistryObject<Item> WATER_BOWL = ITEMS.register("water_bowl", () -> new WaterBowlItem(
            new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL)));
    public static final RegistryObject<Item> LEATHER_FLASK = ITEMS.register("leather_flask", () ->
            new FlaskItem(FlaskTiers.LEATHER));
    public static final RegistryObject<Item> COPPER_FLASK = ITEMS.register("copper_flask", () ->
            new FlaskItem(FlaskTiers.COPPER));
    public static final RegistryObject<Item> IRON_FLASK = ITEMS.register("iron_flask", () ->
            new FlaskItem(FlaskTiers.IRON));
    public static final RegistryObject<Item> GOLDEN_FLASK = ITEMS.register("golden_flask", () ->
            new FlaskItem(FlaskTiers.GOLD));
    public static final RegistryObject<Item> DIAMOND_FLASK = ITEMS.register("diamond_flask", () ->
            new FlaskItem(FlaskTiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_FLASK = ITEMS.register("netherite_flask", () ->
            new FlaskItem(FlaskTiers.NETHERITE));

    public static final RegistryObject<Item> MUG_ITEM = ITEMS.register("mug", () ->
            new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> CUP_ITEM = ITEMS.register("cup", () ->
            new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BEER_MUG_ITEM = ITEMS.register("beer_mug", () ->
            new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> COOKING_POT_ITEM = ITEMS.register("cooking_pot_block", () ->
            new CookingPotBlockItem(ModBlocks.COOKING_POT.get(), new Item.Properties()));
    public static final RegistryObject<Item> FRYING_PAN_ITEM = ITEMS.register("frying_pan_block", () ->
            new FryingPanBlockItem(ModBlocks.FRYING_PAN.get(), new Item.Properties()));
    public static final RegistryObject<Item> TEAPOT_ITEM = ITEMS.register("teapot_block", () ->
            new TeapotBlockItem(ModBlocks.TEAPOT.get(), new Item.Properties()));


    //Edibles
    public static final RegistryObject<Item> CARAMEL_APPLE = ITEMS.register("caramel_apple",
            () -> new Item(new Item.Properties().food(ModFoodProperties.CARAMEL_APPLE)));

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
