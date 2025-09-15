package net.shawdy.alacarte.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ALLOW_POSITIVE;
    public static final ForgeConfigSpec.BooleanValue ALLOW_NEGATIVE;
    public static final ForgeConfigSpec.BooleanValue ALLOW_DEATH_PENALTY;
    public static final ForgeConfigSpec.DoubleValue DEATH_PENALTY;

    static {
        BUILDER.push("tmcfm_effects");
        ALLOW_POSITIVE = BUILDER
                .comment("Apply positive diet effects")
                .define("allow_positive", true);
        ALLOW_NEGATIVE = BUILDER
                .comment("Apply negative diet effects")
                .define("allow_negative", true);
        BUILDER.pop();
        BUILDER.push("tmcfm_death");
        ALLOW_DEATH_PENALTY = BUILDER
                .comment("ALlow decreasing or increasing diet values upon death \n" +
                        "Penalty does not increase value higher than 50")
                .define("allow_death_penalty", false);
        DEATH_PENALTY = BUILDER
                .comment("Define death penalty")
                .defineInRange("death_penalty", 10.0, -100, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}