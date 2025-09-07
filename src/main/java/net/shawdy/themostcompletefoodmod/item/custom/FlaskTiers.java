package net.shawdy.themostcompletefoodmod.item.custom;



public enum FlaskTiers {
    LEATHER(0, 5),
    COPPER(1, 10),
    IRON(2, 20),
    GOLD(3, 30),
    DIAMOND(4, 50),
    NETHERITE(5, 100);

    private final int level;
    private final int uses;

    FlaskTiers(int pLevel, int pUses) {
        level = pLevel;
        uses = pUses;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxUses() {
        return uses;
    }
}
