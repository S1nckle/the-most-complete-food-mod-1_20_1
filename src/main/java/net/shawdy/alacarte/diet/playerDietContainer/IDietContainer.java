package net.shawdy.alacarte.diet.playerDietContainer;

import net.minecraft.nbt.CompoundTag;

public interface IDietContainer {
    float a_la_carte$getValue(String pId);

    void a_la_carte_1_20_1$setValue(String pid, float pValue);

    void a_la_carte_1_20_1$increaseValue(String pId, float pValue);

    byte[] a_la_carte_1_20_1$getValuesFullness();

    void a_la_carte_1_20_1$resetDietContainer();

    void a_la_carte_1_20_1$applyPenalty(float penalty);

    CompoundTag a_la_carte_1_20_1$save();

    void a_la_carte_1_20_1$load(CompoundTag pCompound);


}
