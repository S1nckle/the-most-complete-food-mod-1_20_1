package net.shawdy.themostcompletefoodmod.ui;

public interface IPlayerDietContainer {
    float proteins();
    float fats();
    float carbogydrates();
    float fiber();
    float minerals();
    float vitamins();
    float water();

    float max_proteins = 100.0f;
    float max_fats = 100.0f;
    float max_carbogydrates = 100.0f;
    float max_fiber = 100.0f;
    float max_minerals = 100.0f;
    float max_vitamins = 100.0f;
    float max_water = 100.0f;

    float the_most_complete_food_mod_1_20_1$getValue(int pId);
    void the_most_complete_food_mod_1_20_1$setValue(int pid, float pValue);
    void the_most_complete_food_mod_1_20_1$increaseValue(int pId, float pValue);
    void the_most_complete_food_mod_1_20_1$decreaseValue(int pId, float pValue);
}
