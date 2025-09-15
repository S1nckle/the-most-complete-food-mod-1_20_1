package net.shawdy.alacarte.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.diet.DietValuesHolder;
import net.shawdy.alacarte.diet.FoodDietValuesManager;
import net.shawdy.alacarte.diet.playerDietContainer.IDietContainer;
import net.shawdy.alacarte.item.custom.FlaskItem;

@Mod.EventBusSubscriber(
        modid = ALaCarte.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class CommonModEvents {

    @SubscribeEvent
    public static void onDataLoad(AddReloadListenerEvent pEvent) {
        pEvent.addListener(new ResourceManagerReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager pManager) {
                FoodDietValuesManager.load(pManager);
            }
        });
    }

    @SubscribeEvent
    public static void onPotionDrink(LivingEntityUseItemEvent.Finish pEvent) {
        if (!(pEvent.getEntity() instanceof Player pPlayer)) return;
        Item pItem = pEvent.getItem().getItem();
        if (pItem.isEdible()) return;
        ResourceLocation pLocation = ForgeRegistries.ITEMS.getKey(pItem);
        DietValuesHolder data = FoodDietValuesManager.getData(pLocation);
        if (data != null) {
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("proteins", data.getProteins());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("fats", data.getFats());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("carbohydrates", data.getCarbohydrates());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("fiber", data.getFiber());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("minerals", data.getMinerals());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("vitamins", data.getVitamins());
            ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("water", data.getWater());
        }
    }

    @SubscribeEvent
    public static void onFlaskCrafting(PlayerEvent.ItemCraftedEvent pEvent) {
        ItemStack pStack = pEvent.getCrafting();
        if (pStack.getItem() instanceof FlaskItem flask) {
            pStack.setDamageValue(flask.getMaxDamage(pStack));
        }
    }

}
