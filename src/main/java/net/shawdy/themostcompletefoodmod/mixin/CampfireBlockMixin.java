package net.shawdy.themostcompletefoodmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shawdy.themostcompletefoodmod.init.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin extends Block {


    @Shadow @Final protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);

    public CampfireBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Unique
    private static final VoxelShape the_most_complete_food_mod_1_20_1$PLATE = Block.box(0, 15, 0, 16, 16, 16);
    @Unique
    private static final VoxelShape the_most_complete_food_mod_1_20_1$LEGS = Shapes.or(Block.box(0, 0, 0, 1, 15, 1),
            Block.box(15, 0, 0, 16, 15, 1),
            Block.box(0, 0, 15, 1, 15, 16),
            Block.box(15, 0, 15, 16, 15, 16));

    @Unique
    private static final VoxelShape SUPPORT_SHAPE = Shapes.or(the_most_complete_food_mod_1_20_1$PLATE, the_most_complete_food_mod_1_20_1$LEGS);

    @Unique
    private static final BooleanProperty SUPPORTABLE_PLACED = BooleanProperty.create("supportable_placed");

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(boolean pSpawnParticles, int pFireDamage, Properties pProperties, CallbackInfo ci) {
        this.registerDefaultState(this.stateDefinition.any().setValue(SUPPORTABLE_PLACED, false));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    protected void onCreateBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SUPPORTABLE_PLACED);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(SUPPORTABLE_PLACED)) {
            return Shapes.or(SHAPE, SUPPORT_SHAPE);
        }
        return SHAPE;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        the_most_complete_food_mod_1_20_1$updateSpecialBlockState(pLevel, pPos);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
        the_most_complete_food_mod_1_20_1$updateSpecialBlockState(pLevel, pPos);
    }

    @Unique
    private void the_most_complete_food_mod_1_20_1$updateSpecialBlockState(Level pLevel, BlockPos pPos) {
        BlockState pState = pLevel.getBlockState(pPos);
        BlockState pAbove = pLevel.getBlockState(pPos.above());

        boolean supPlaced = pAbove.is(ModBlocks.COOKING_POT.get()) ||
                            pAbove.is(ModBlocks.FRYING_PAN.get()) ||
                            pAbove.is(ModBlocks.TEAPOT.get());

        if (pState.getValue(SUPPORTABLE_PLACED) != supPlaced) {
            pLevel.setBlock(pPos, pState.setValue(SUPPORTABLE_PLACED, supPlaced), 3);
        }
    }
}
