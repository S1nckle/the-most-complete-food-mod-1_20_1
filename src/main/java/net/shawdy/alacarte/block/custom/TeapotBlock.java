package net.shawdy.alacarte.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.shawdy.alacarte.block.entity.TeapotBlockEntity;
import org.jetbrains.annotations.Nullable;

public class TeapotBlock extends Block implements EntityBlock {

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
        if (isHeat(pLevel, pPos) && pLevel.isClientSide()) {
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

    public static boolean isHeat(Level pLevel, BlockPos pPos) {
        BlockState blockBelow = pLevel.getBlockState(pPos.below());
        if (blockBelow.is(Blocks.CAMPFIRE) || blockBelow.is(Blocks.SOUL_CAMPFIRE)) {
            return blockBelow.getValue(CampfireBlock.LIT);
        }

        return false;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof TeapotBlockEntity teapot) {
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), teapot, pPos);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TeapotBlockEntity(pPos, pState);
    }
}
