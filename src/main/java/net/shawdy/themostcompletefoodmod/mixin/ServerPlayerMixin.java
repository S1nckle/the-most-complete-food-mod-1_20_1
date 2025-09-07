package net.shawdy.themostcompletefoodmod.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shawdy.themostcompletefoodmod.config.CommonModConfig;
import net.shawdy.themostcompletefoodmod.diet.playerDietContainer.IDietContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class  ServerPlayerMixin {

    private ServerPlayer self() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(method = "restoreFrom (Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void restoreFrom(ServerPlayer pThat, boolean pKeepEverything, CallbackInfo ci) {
        CompoundTag old = ((IDietContainer) pThat.getFoodData()).the_most_complete_food_mod_1_20_1$save();
        if (old != null) {
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod_1_20_1$load(old);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("water", 100.0f);
            if (CommonModConfig.ALLOW_DEATH_PENALTY.get()) {
                float penalty = CommonModConfig.DEATH_PENALTY.get().floatValue();
                ((IDietContainer) self().getFoodData()).the_most_complete_food_mod_1_20_1$applyPenalty(penalty);
            }
        }
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "TAIL"))
    private void drop(ItemStack pDroppedItem, boolean pDropAround, boolean pTraceItem, CallbackInfoReturnable<ItemEntity> cir) {
        if (pDroppedItem.getItem() == Items.DIAMOND) {
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod_1_20_1$resetDietContainer();
            self().getFoodData().setFoodLevel(20);
            self().getFoodData().setSaturation(20.0f);
        } else
        if(pDroppedItem.getItem() == Items.EMERALD) {
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("proteins", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("fats", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("carbohydrates", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("fiber", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("minerals", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("vitamins", 100);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("water", 100);
            self().getFoodData().setFoodLevel(20);
            self().getFoodData().setSaturation(20.0f);
        } else
        if(pDroppedItem.getItem() == Items.COAL) {
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("proteins", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("fats", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("carbohydrates", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("fiber", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("minerals", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("vitamins", 0);
            ((IDietContainer) self().getFoodData()).the_most_complete_food_mod$setValue("water", 0);
            }

    }
}
