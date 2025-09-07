package net.shawdy.themostcompletefoodmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TeapotBlock extends Block {

    public TeapotBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final VoxelShape SHAPE = Block.box(3.5, 0, 3.5, 12.5, 8, 12.5);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState below = pLevel.getBlockState(pPos.below());
        return below.isFaceSturdy(pLevel, pPos.below(), Direction.UP, SupportType.FULL) || below.is(Blocks.CAMPFIRE)
               || below.is(Blocks.SOUL_CAMPFIRE);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isHeat(pLevel, pPos.below()) && pLevel.isClientSide()) {
            spawnSteamParticles(pLevel, pPos, pRandom);
        }
    }

    private void spawnSteamParticles(Level pLevel, BlockPos pPos, RandomSource pRandom) {
        double x, z;
        Direction facing = pLevel.getBlockState(pPos).getValue(FACING);
        if (facing == Direction.NORTH) {
            x = 0.5;
            z = 0.1;
        } else if (facing == Direction.EAST) {
            x = 0.9;
            z = 0.5;
        } else if (facing == Direction.SOUTH) {
            x = 0.5;
            z = 0.9;
        } else {
            x = 0.1;
            z = 0.5;
        }
        double offset = 0.05; // Очень маленькое смещение
        for (int i = 0; i < 20; i++) {
            pLevel.addParticle(ParticleTypes.SMOKE,
                    pPos.getX() + x,
                    pPos.getY() + 0.3,
                    pPos.getZ() + z,
                    0.0, 0.05, 0.0);
        }
    }

    private boolean isHeat(Level pLevel, BlockPos pPos) {
        BlockState blockBelow = pLevel.getBlockState(pPos);
        if (blockBelow.is(Blocks.CAMPFIRE) || blockBelow.is(Blocks.SOUL_CAMPFIRE)) {
            return blockBelow.getValue(CampfireBlock.LIT);
        }

        return false;
    }

}
