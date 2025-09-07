package net.shawdy.themostcompletefoodmod.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shawdy.themostcompletefoodmod.TheMostCompleteFoodMod;
import net.shawdy.themostcompletefoodmod.block.entity.CuttingBoardBlockEntity;
import net.shawdy.themostcompletefoodmod.block.entity.FryingPanBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TheMostCompleteFoodMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<FryingPanBlockEntity>> FRYING_PAN_BE =
            BLOCK_ENTITIES.register("frying_block_block_entity", () -> BlockEntityType.Builder.of(
                    FryingPanBlockEntity::new,
                    ModBlocks.FRYING_PAN.get()).build(null));
    public static final RegistryObject<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD_BE =
            BLOCK_ENTITIES.register("cutting_board_block_entity", () -> BlockEntityType.Builder.of(
                    CuttingBoardBlockEntity::new,
                    ModBlocks.CUTTING_BOARD.get()).build(null));


    public static void register(IEventBus pEventBus) {
        BLOCK_ENTITIES.register(pEventBus);
    }
}
