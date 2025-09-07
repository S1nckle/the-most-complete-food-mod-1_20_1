package net.shawdy.themostcompletefoodmod.diet.playerDietContainer;

import net.minecraft.nbt.CompoundTag;

public interface IDietContainer {
    float the_most_complete_food_mod$getValue(String pId);

    void the_most_complete_food_mod$setValue(String pid, float pValue);

    void the_most_complete_food_mod$increaseValue(String pId, float pValue);

    byte[] the_most_complete_food_mod_1_20_1$getValuesFullness();

    void the_most_complete_food_mod_1_20_1$resetDietContainer();

    void the_most_complete_food_mod_1_20_1$applyPenalty(float penalty);

    CompoundTag the_most_complete_food_mod_1_20_1$save();

    void the_most_complete_food_mod_1_20_1$load(CompoundTag pCompound);


}
