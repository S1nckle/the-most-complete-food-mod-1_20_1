package net.shawdy.themostcompletefoodmod.diet;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class DietAttributes {
    public static final AttributeModifier PROTEINS_EXTRA_DAMAGE = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5a"),
            "tmcfm.proteins_extra_damage",
            1.0,
            AttributeModifier.Operation.ADDITION
    );
    public static final AttributeModifier CARBOHYDRATES_EXTRA_MOVEMENT_SPEED = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5d"),
            "tmcfm.carbohydrates_extra_speed",
            0.01,
            AttributeModifier.Operation.ADDITION
    );
    public static final AttributeModifier FATS_EXTRA_HEALTH = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5c"),
            "tmcfm.fats_extra_health",
            4.0,
            AttributeModifier.Operation.ADDITION
    );
    public static final AttributeModifier MINERALS_KNOCKBACK_RESISTANCE = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5e"),
            "tmcfm.vitamins_extra_health",
            0.3,
            AttributeModifier.Operation.ADDITION
    );
    public static final AttributeModifier VITAMINS_EXTRA_HEALTH = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5b"),
            "tmcfm.vitamins_extra_health",
            4.0,
            AttributeModifier.Operation.ADDITION
    );

    public static final AttributeModifier WATER_MOVEMENT_SLOWNESS = new AttributeModifier(
            UUID.fromString("d3a4a5fd-7d27-4b18-872d-6a1e8d3e9f5f"),
            "tmcfm.water_movement_slowness",
            -0.01,
            AttributeModifier.Operation.ADDITION
    );
}
