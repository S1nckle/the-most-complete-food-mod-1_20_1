package net.shawdy.alacarte.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.shawdy.alacarte.config.CommonModConfig;
import net.shawdy.alacarte.diet.DietAttributes;
import net.shawdy.alacarte.diet.DietValuesHolder;
import net.shawdy.alacarte.diet.FoodDietValuesManager;
import net.shawdy.alacarte.diet.playerDietContainer.IDietContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements IDietContainer {

    @Shadow public abstract void addExhaustion(float pExhaustion);
    @Shadow public abstract float getExhaustionLevel();

    @Unique
    private float proteins = 50.0f;
    @Unique
    private float fats = 50.0f;
    @Unique
    private float carbohydrates = 50.0f;
    @Unique
    private float fiber = 50.0f;
    @Unique
    private float minerals = 50.0f;
    @Unique
    private float vitamins = 50.0f;
    @Unique
    private float water = 100.0f;

    public final float max_proteins = 100.0f;
    public final float max_fats = 100.0f;
    public final float max_carbogydrates = 100.0f;
    public final float max_fiber = 100.0f;
    public final float max_minerals = 100.0f;
    public final float max_vitamins = 100.0f;
    public final float max_water = 100.0f;

    @Unique
    private FoodData self() {
        return (FoodData) (Object) this;
    }
    @Unique
    private int lastFoodLevel = self().getLastFoodLevel();
    private final Random random = new Random();
    @Unique
    private int tickTimer = 0;
    private boolean shouldApplyNegativeEffects = CommonModConfig.ALLOW_NEGATIVE.get();
    private boolean shouldApplyPositiveEffects = CommonModConfig.ALLOW_POSITIVE.get();


    public float the_most_complete_food_mod$getValue(String pId) {
        return switch (pId) {
            case "proteins" -> this.proteins;
            case "fats" -> this.fats;
            case "carbohydrates" -> this.carbohydrates;
            case "fiber" -> this.fiber;
            case "minerals" -> this.minerals;
            case "vitamins" -> this.vitamins;
            case "water" -> this.water;
            default -> 0.0f;
        };
    }

    public void the_most_complete_food_mod$setValue(String pId, float pValue) {
        switch (pId) {
            case "proteins":
                this.proteins = pValue;
                break;
            case "fats":
                this.fats = pValue;
                break;
            case "carbohydrates":
                this.carbohydrates = pValue;
                break;
            case "fiber":
                this.fiber = pValue;
                break;
            case "minerals":
                this.minerals = pValue;
                break;
            case "vitamins":
                this.vitamins = pValue;
                break;
            case "water":
                this.water = pValue;
                break;
            default:
                break;
        }
    }

    public void the_most_complete_food_mod$increaseValue(String pId, float pValue) {
        switch (pId) {
            case "proteins":
                this.proteins = Math.min(this.proteins + pValue, max_proteins);
                break;
            case "fats":
                this.fats = Math.min(this.fats + pValue, max_fats);
                break;
            case "carbohydrates":
                this.carbohydrates = Math.min(this.carbohydrates + pValue, max_carbogydrates);
                break;
            case "fiber":
                this.fiber = Math.min(this.fiber + pValue, max_fiber);
                break;
            case "minerals":
                this.minerals = Math.min(this.minerals + pValue, max_minerals);
                break;
            case "vitamins":
                this.vitamins = Math.min(this.vitamins + pValue, max_vitamins);
                break;
            case "water":
                this.water = Math.min(this.water + pValue, max_water);
                break;
            default:
                break;

        }
    }

    public byte[] the_most_complete_food_mod_1_20_1$getValuesFullness() {
        return new byte[]{
                the_most_complete_food_mod_1_20_1$getFullness(proteins, max_proteins),
                the_most_complete_food_mod_1_20_1$getFullness(fats, max_fats),
                the_most_complete_food_mod_1_20_1$getFullness(carbohydrates, max_carbogydrates),
                the_most_complete_food_mod_1_20_1$getFullness(fiber, max_fiber),
                the_most_complete_food_mod_1_20_1$getFullness(minerals, max_minerals),
                the_most_complete_food_mod_1_20_1$getFullness(vitamins, max_vitamins),
                the_most_complete_food_mod_1_20_1$getFullness(water, max_water)
        };
    }

    public void the_most_complete_food_mod_1_20_1$resetDietContainer() {
        this.proteins = 50.0f;
        this.fats = 50.0f;
        this.carbohydrates = 50.0f;
        this.fiber = 50.0f;
        this.minerals = 50.0f;
        this.vitamins = 50.0f;
        this.water = 100.0f;
    }

    public void the_most_complete_food_mod_1_20_1$applyPenalty(float penalty) {
        this.proteins = this.proteins > 50 ? this.proteins : Math.min(this.proteins + penalty, 50);
        this.fats = this.fats > 50 ? this.fats : Math.min(this.fats + penalty, 50);
        this.carbohydrates = this.carbohydrates > 50 ? this.carbohydrates : Math.min(this.carbohydrates + penalty, 50);
        this.fiber = this.fiber > 50 ? this.fiber : Math.min(this.fiber + penalty, 50);
        this.minerals = this.minerals > 50 ? this.minerals : Math.min(this.minerals + penalty, 50);
        this.vitamins = this.vitamins > 50 ? this.vitamins : Math.min(this.vitamins + penalty, 50);
    }

    @Unique
    private byte the_most_complete_food_mod_1_20_1$getFullness(float value, float max_value) {
        return (byte) Math.round(((value / max_value) * 100));
    }

    @Unique
    private boolean the_most_complete_food_mod_1_20_1$canRegenerate(Player pPlayer) {
        return this.proteins >= 5 && this.vitamins > 5 && this.minerals > 5 || pPlayer.level().getDifficulty() == Difficulty.PEACEFUL || !CommonModConfig.ALLOW_NEGATIVE.get();
    }

    @Unique
    private void the_most_complete_food_mod_1_20_1$tick(Player pPlayer) {
        int foodLevel = self().getFoodLevel();

        /**
         * Default modifiers, applied when foodLevel is decreasing
         */
        if (this.lastFoodLevel > foodLevel) {
            this.proteins = Math.max(0, this.proteins - 0.15f);
            this.fats = Math.max(0, this.fats - 0.2f);
            this.carbohydrates = Math.max(0, this.carbohydrates - 1.0f);
            this.fiber = Math.max(0, this.fiber - 0.3f);
            this.minerals = Math.max(0, this.minerals - 0.1f);
            this.vitamins = Math.max(0, this.vitamins - 0.08f);
            this.minerals = Math.max(0, this.minerals - 0.02f);

            /**
             * If proteins or fats are low, vitamins are decaying faster
             */
            if (proteins < 25) {
                this.vitamins = Math.max(0, this.vitamins - 0.125f);
            }

            /**
             * If proteins or fats are low, vitamins are decaying faster
              */

            if (fats < 25) {
                this.vitamins = Math.max(0, this.vitamins - 0.125f);
            }

            /**
             * If carbs are low, fats become energy source.
             * If fats low too, proteins do.
              */

            if (this.carbohydrates < 25) {
                if (this.fats >= 25) {
                    this.carbohydrates = Math.min(100, this.carbohydrates + 0.75f);
                    this.fats = Math.max(0, this.fats - 1.0f);
                } else if (this.proteins >= 25) {
                    this.proteins = Math.max(0, this.proteins - 0.7f);
                    this.carbohydrates = Math.max(0, this.carbohydrates - 0.7f);
                }
            }
        }
        /**
         * water logic
         */
        if (getExhaustionLevel() > 4.0) {
            this.water = Math.max(0, this.water - 0.5f);
        }
        this.water = Math.max(0, this.water - 0.005f);
        if (pPlayer.hasEffect(MobEffects.POISON)) {
            this.water = Math.max(0, this.water - 0.1f);
            this.minerals = Math.max(0, this.minerals - 0.01f);
        }

        /**
         * When starving, fats are used more frequently
         */
        if (foodLevel < 6 && fats >= 10) {
            this.fats = Math.max(0, this.fats - 1.0f);
            this.water = Math.max(0, this.water - 0.5f);
            this.carbohydrates = Math.min(100, this.carbohydrates + 1.0f);
            self().setFoodLevel(foodLevel + 1);
        }
        this.lastFoodLevel = foodLevel;
    }

    @Unique
    private void the_most_complete_food_mod_1_20_1$applyEffects(Player pPlayer) {
        if (this.shouldApplyNegativeEffects) {
            if (this.fats < 15) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,
                        1, 0, false, true, false));
            }
            if (this.carbohydrates < 15) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                        1, 0, false, true, false));
            }
            if (this.fiber < 15) {
                if (random.nextInt(0, 500) == 1) {
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION,
                            60, 0, false, true, false));
                }
            }
            if (this.water < 10) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,
                        1, 0, false, false, false));
                if (!pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(DietAttributes.WATER_MOVEMENT_SLOWNESS)) {
                    pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(DietAttributes.WATER_MOVEMENT_SLOWNESS);
                } else if (pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(DietAttributes.WATER_MOVEMENT_SLOWNESS)) {
                    pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(DietAttributes.WATER_MOVEMENT_SLOWNESS);
                }
            }

            /**
             * Let player eat to restore diet values if he is hit or fiber or fats are low
             */
            if ((pPlayer.isHurt() && !the_most_complete_food_mod_1_20_1$canRegenerate(pPlayer))
                    && self().getFoodLevel() == 20) {
                addExhaustion(1.0f);
            }
        }

        if (this.water == 0) {
            this.tickTimer++;
            if (this.tickTimer >= 80) {
                if (pPlayer.getHealth() > 10.0F || pPlayer.level().getDifficulty() == Difficulty.HARD ||
                        pPlayer.getHealth() > 1.0F && pPlayer.level().getDifficulty() == Difficulty.NORMAL)
                    pPlayer.hurt(pPlayer.damageSources().starve(), 1.0f);
                this.tickTimer = 0;
            }
        }

        if (this.shouldApplyPositiveEffects) {
            if (this.proteins >= 90 && !pPlayer.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(DietAttributes.PROTEINS_EXTRA_DAMAGE)) {
                pPlayer.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(DietAttributes.PROTEINS_EXTRA_DAMAGE);
            } else if(this.proteins < 90 && pPlayer.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(DietAttributes.PROTEINS_EXTRA_DAMAGE)) {
                pPlayer.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DietAttributes.PROTEINS_EXTRA_DAMAGE);
            }
            if (this.fats >= 90 && !pPlayer.getAttribute(Attributes.MAX_HEALTH).hasModifier(DietAttributes.FATS_EXTRA_HEALTH)) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(DietAttributes.FATS_EXTRA_HEALTH);
            } else if(this.fats < 90 && pPlayer.getAttribute(Attributes.MAX_HEALTH).hasModifier(DietAttributes.FATS_EXTRA_HEALTH)) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).removeModifier(DietAttributes.FATS_EXTRA_HEALTH);
                if (pPlayer.getHealth() > pPlayer.getMaxHealth()) {
                    pPlayer.setHealth(pPlayer.getMaxHealth());
                }
            }
            if (this.carbohydrates >= 90 && !pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(DietAttributes.CARBOHYDRATES_EXTRA_MOVEMENT_SPEED)) {
                pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(DietAttributes.CARBOHYDRATES_EXTRA_MOVEMENT_SPEED);
            } else if(this.carbohydrates < 90 && pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(DietAttributes.CARBOHYDRATES_EXTRA_MOVEMENT_SPEED)) {
                pPlayer.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(DietAttributes.CARBOHYDRATES_EXTRA_MOVEMENT_SPEED);
            }
            if (this.minerals >= 90) {
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1,
                        0, false, false, false));
            }
            if (this.vitamins >= 90 && !pPlayer.getAttribute(Attributes.MAX_HEALTH).hasModifier(DietAttributes.VITAMINS_EXTRA_HEALTH)) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(DietAttributes.VITAMINS_EXTRA_HEALTH);
            } else if(this.vitamins < 90 && pPlayer.getAttribute(Attributes.MAX_HEALTH).hasModifier(DietAttributes.VITAMINS_EXTRA_HEALTH)) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).removeModifier(DietAttributes.VITAMINS_EXTRA_HEALTH);
                if (pPlayer.getHealth() > pPlayer.getMaxHealth()) {
                    pPlayer.setHealth(pPlayer.getMaxHealth());
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(Player pPlayer, CallbackInfo ci) {
        if (pPlayer.level().getDifficulty() != Difficulty.PEACEFUL && !pPlayer.isCreative()) {
                the_most_complete_food_mod_1_20_1$tick(pPlayer);
                the_most_complete_food_mod_1_20_1$applyEffects(pPlayer);
        }
    }

    //Whenever player heals, he spends proteins, minerals and vitamins
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V"))
    private void healTwo(Player pPlayer, CallbackInfo ci) {
        this.proteins = Math.max(0, this.proteins - 0.2f);
        this.vitamins = Math.max(0, this.vitamins - 0.075f);
        this.minerals = Math.max(0, this.minerals - 0.1f);
    }

    //Can't regenerate when proteins, vitamins or minerals are low
    @ModifyVariable(method = "tick", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private boolean modifyRegenFlag(boolean originalFlag, Player pPlayer) {
        return originalFlag && this.the_most_complete_food_mod_1_20_1$canRegenerate(pPlayer);
    }

    @Inject(method = "eat (Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "TAIL"), remap = false)
    private void eat(Item pItem, ItemStack pStack, LivingEntity entity, CallbackInfo ci) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(pStack.getItem());
        DietValuesHolder data = FoodDietValuesManager.getData(itemId);
        if (data != null) {
            if (this.fiber < 10 && shouldApplyNegativeEffects) {
                //low fiber leads to indigestion which is shown by lowering gain of PFC
                this.proteins = data.getProteins() != 0 ? Math.min(100, this.proteins + data.getProteins() * 0.6f) : this.proteins;
                this.fats = data.getFats() != 0 ? Math.min(100, this.fats + data.getFats()* 0.6f) : this.fats;
                this.carbohydrates = data.getCarbohydrates() != 0 ? Math.min(100, this.carbohydrates + data.getCarbohydrates() * 0.6f) : this.carbohydrates;
            } else if (this.fiber >= 10 && shouldApplyPositiveEffects) {
                this.proteins = data.getProteins() != 0 ? Math.min(100, this.proteins + data.getProteins() * 1.3f) : this.proteins;
                this.fats = data.getFats() != 0 ? Math.min(100, this.fats + data.getFats() * 1.3f) : this.fats;
                this.carbohydrates = data.getCarbohydrates() != 0 ? Math.min(100, this.carbohydrates + data.getCarbohydrates() * 1.3f) : this.carbohydrates;
            } else {
                this.proteins = data.getProteins() != 0 ? Math.min(100, this.proteins + data.getProteins()) : this.proteins;
                this.fats = data.getFats() != 0 ? Math.min(100, this.fats + data.getFats()) : this.fats;
                this.carbohydrates = data.getCarbohydrates() != 0 ? Math.min(100, this.carbohydrates + data.getCarbohydrates()) : this.carbohydrates;
            }
            this.fiber = data.getFiber() != 0 ? Math.min(100, this.fiber + data.getFiber()) : this.fiber;
            this.minerals = data.getMinerals() != 0 ? Math.min(100, this.minerals + data.getMinerals()) : this.minerals;
            this.vitamins = data.getVitamins() != 0 ? Math.min(100, this.vitamins + data.getVitamins()) : this.vitamins;
            this.water = data.getWater() != 0 ? Math.min(100, this.water + data.getWater()) : this.water;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    private void addDietContainerData(CompoundTag pCompound, CallbackInfo ci) {
        CompoundTag dietValuesTag = new CompoundTag();
        dietValuesTag.putFloat("Proteins", this.proteins);
        dietValuesTag.putFloat("Fats", this.fats);
        dietValuesTag.putFloat("Carbohydrates", this.carbohydrates);
        dietValuesTag.putFloat("Fiber", this.fiber);
        dietValuesTag.putFloat("Minerals", this.minerals);
        dietValuesTag.putFloat("Vitamins", this.vitamins);
        dietValuesTag.putFloat("Water", this.water);
        pCompound.put("dietValues", dietValuesTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    private void readDietContainerData(CompoundTag pCompound, CallbackInfo ci) {
        if (pCompound.contains("dietValues")) {
            CompoundTag dietValuesTag = pCompound.getCompound("dietValues");
            this.proteins = dietValuesTag.getFloat("Proteins");
            this.fats = dietValuesTag.getFloat("Fats");
            this.carbohydrates = dietValuesTag.getFloat("Carbohydrates");
            this.fiber = dietValuesTag.getFloat("Fiber");
            this.minerals = dietValuesTag.getFloat("Minerals");
            this.vitamins = dietValuesTag.getFloat("Vitamins");
            this.water = dietValuesTag.getFloat("Water");
        } else {
            this.proteins = 50.0f;
            this.fats = 50.0f;
            this.carbohydrates = 50.0f;
            this.fiber = 50.0f;
            this.minerals = 50.0f;
            this.vitamins = 50.0f;
            this.water = 50.0f;
        }
    }

    public CompoundTag the_most_complete_food_mod_1_20_1$save() {
        CompoundTag dietValuesTag = new CompoundTag();
        dietValuesTag.putFloat("Proteins", this.proteins);
        dietValuesTag.putFloat("Fats", this.fats);
        dietValuesTag.putFloat("Carbohydrates", this.carbohydrates);
        dietValuesTag.putFloat("Fiber", this.fiber);
        dietValuesTag.putFloat("Minerals", this.minerals);
        dietValuesTag.putFloat("Vitamins", this.vitamins);
        dietValuesTag.putFloat("Water", this.water);
        return dietValuesTag;
    }

    public void the_most_complete_food_mod_1_20_1$load(CompoundTag dietValuesTag) {
        this.proteins = dietValuesTag.getFloat("Proteins");
        this.fats = dietValuesTag.getFloat("Fats");
        this.carbohydrates = dietValuesTag.getFloat("Carbohydrates");
        this.fiber = dietValuesTag.getFloat("Fiber");
        this.minerals = dietValuesTag.getFloat("Minerals");
        this.vitamins = dietValuesTag.getFloat("Vitamins");
        this.water = dietValuesTag.getFloat("Water");
    }
}
