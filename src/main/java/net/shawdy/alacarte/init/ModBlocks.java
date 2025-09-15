package net.shawdy.alacarte.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.alacarte.ALaCarte;
import net.shawdy.alacarte.block.custom.*;

import java.util.function.Supplier;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ALaCarte.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> blockToRegister = BLOCKS.register(name, block);
        ModItems.registerBlockItem(name, blockToRegister);
        return blockToRegister;
    }


    public static final RegistryObject<Block> MIXING_BOWL = registerBlock("mixing_bowl_block",
            () -> new MixingBowlBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> COOKING_POT = BLOCKS.register("cooking_pot_block",
            () -> new CookingPotBlock(BlockBehaviour.Properties.of().sound(SoundType.CHAIN).strength(0.5F)));
    public static final RegistryObject<Block> FRYING_PAN = BLOCKS.register("frying_pan_block",
            () -> new FryingPanBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.CHAIN)));
    public static final RegistryObject<Block> TEAPOT = BLOCKS.register("teapot_block",
            () -> new TeapotBlock(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.5F)));
    public static final RegistryObject<Block> CUTTING_BOARD = registerBlock("cutting_board_block",
            () -> new CuttingBoardBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WOOD)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
