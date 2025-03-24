package net.shawdy.themostcompletefoodmod.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.shawdy.themostcompletefoodmod.ui.IPlayerDietContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayerDietContainer {

    @Shadow public abstract void playNotifySound(SoundEvent pSound, SoundSource pSource, float pVolume, float pPitch);

    private float proteins = 20;
    private float fats = 20;
    private float carbogydrates = 20;
    private float fiber = 20;
    private float minerals = 20;
    private float vitamins = 20;
    private float water = 20;

    final float max_proteins = 100.0f;
    final float max_fats = 100.0f;
    final float max_carbogydrates = 100.0f;
    final float max_fiber = 100.0f;
    final float max_minerals = 100.0f;
    final float max_vitamins = 100.0f;
    final float max_water = 100.0f;



    public float the_most_complete_food_mod_1_20_1$getValue(int pId) {
        return switch (pId) {
            case 0 -> this.proteins;
            case 1 -> this.fats;
            case 2 -> this.carbogydrates;
            case 3 -> this.fiber;
            case 4 -> this.minerals;
            case 5 -> this.vitamins;
            case 6 -> this.water;
            default ->
                    throw new NullPointerException("\nat PlayerDietContainer.getValue(): \nTried to get value with Id other from 0 to 6.");
        };
    }

    public void the_most_complete_food_mod_1_20_1$setValue(int pId, float pValue) {
        switch (pId) {
            case 0:
                this.proteins = pValue;
            case 1:
                this.fats = pValue;
            case 2:
                this.carbogydrates = pValue;
            case 3:
                this.fiber = pValue;
            case 4:
                this.minerals = pValue;
            case 5:
                this.vitamins = pValue;
            case 6:
                this.water = pValue;
            default:
                throw new NullPointerException("\nat PlayerDietContainer.setValue(): \nTried to set value with Id other from 0 to 6.");

        }
    }

    public void the_most_complete_food_mod_1_20_1$increaseValue(int pId, float pValue) {
        switch (pId) {
            case 0:
                this.proteins = Math.min(this.proteins + pValue, max_proteins);
            case 1:
                this.fats = Math.min(this.fats + pValue, max_fats);
            case 2:
                this.carbogydrates = Math.min(this.carbogydrates + pValue, max_carbogydrates);
            case 3:
                this.fiber = Math.min(this.fiber + pValue, max_fiber);
            case 4:
                this.minerals = Math.min(this.minerals + pValue, max_minerals);
            case 5:
                this.vitamins = Math.min(this.vitamins + pValue, max_vitamins);
            case 6:
                this.water = Math.min(this.water + pValue, max_water);
            default:
                throw new NullPointerException("\nat PlayerDietContainer.increaseValue(): \nTried to increase value with Id other from 0 to 6.");

        }
    }

    public void the_most_complete_food_mod_1_20_1$decreaseValue(int pId, float pValue) {
        switch (pId) {
            case 0:
                this.proteins = Math.max(this.proteins - pValue, 0.0f);
            case 1:
                this.fats = Math.max(this.fats - pValue, 0.0f);
            case 2:
                this.carbogydrates = Math.max(this.carbogydrates - pValue, 0.0f);
            case 3:
                this.fiber = Math.max(this.fiber - pValue, 0.0f);
            case 4:
                this.minerals = Math.max(this.minerals - pValue, 0.0f);
            case 5:
                this.vitamins = Math.max(this.vitamins - pValue, 0.0f);
            case 6:
                this.water = Math.max(this.water - pValue, 0.0f);
            default:
                throw new NullPointerException("\nat PlayerDietContainer.decreaseValue(): \nTried to increase value with Id other from 0 to 6.");

        }
    }

    @Unique
    public void the_most_complete_food_mod_1_20_1$resetDietContainer() {
        this.proteins = 0;
        this.fats = 0;
        this.carbogydrates = 0;
        this.fiber = 0;
        this.minerals = 0;
        this.vitamins = 0;
        this.water = 0;

    }

    @Inject(method = "die", at = @At("TAIL"))
    private void resetDietContainer(CallbackInfo ci) {
        this.the_most_complete_food_mod_1_20_1$resetDietContainer();
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V"))
    private void addDietContainerData(CompoundTag pCompound, CallbackInfo ci) {
        pCompound.putFloat("Proteins", this.proteins);
        pCompound.putFloat("Fats", this.fats);
        pCompound.putFloat("Carbohydrates", this.carbogydrates);
        pCompound.putFloat("Fiber", this.fiber);
        pCompound.putFloat("Minerals", this.minerals);
        pCompound.putFloat("Vitamins", this.vitamins);
        pCompound.putFloat("Water", this.water);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;load(Lnet/minecraft/nbt/ListTag;)V"))
    private void readDietContainerData(CompoundTag pCompound, CallbackInfo ci) {
        this.proteins = pCompound.getFloat("Proteins");
        this.fats = pCompound.getFloat("Fats");
        this.carbogydrates = pCompound.getFloat("Carbohydrates");
        this.fiber = pCompound.getFloat("Fiber");
        this.minerals = pCompound.getFloat("Minerals");
        this.vitamins = pCompound.getFloat("Vitamins");
        this.water = pCompound.getFloat("Water");
    }

    private Player self() {
        return (Player) (Object) this;
    }





    @Inject(method = "tick", at = @At("TAIL"))
    private void writeProteins(CallbackInfo ci) {
        if(!self().level().isClientSide()) {
            self().sendSystemMessage(Component.literal("Proteins level is: " + this.the_most_complete_food_mod_1_20_1$getValue(0)));
        }
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("TAIL"))
    private void increaseProteins(ItemStack pDroppedItem, boolean pDropAround, boolean pIncludeThrowerName, CallbackInfoReturnable<ItemEntity> cir) {
        if (pDroppedItem.getItem() == Items.DIAMOND) {
            this.the_most_complete_food_mod_1_20_1$increaseValue(0, 0.5f);
        } else {
            this.the_most_complete_food_mod_1_20_1$decreaseValue(0, 0.5f);
        }
    }



}
