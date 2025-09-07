package net.shawdy.themostcompletefoodmod.block.custom;

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
import net.shawdy.themostcompletefoodmod.block.entity.CookingPotBlockEntity;
import org.jetbrains.annotations.Nullable;

public class CookingPotBlock extends Block implements EntityBlock{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 8, 13);


    public CookingPotBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

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
        for (int i = 0; i < 2; i++) { // Уменьшили количество дыма
            double x = pPos.getX() + 0.5 + (pRandom.nextDouble() - 0.5) * 0.4;
            double y = pPos.getY() + 0.3 + pRandom.nextDouble() * 0.05;
            double z = pPos.getZ() + 0.5 + (pRandom.nextDouble() - 0.5) * 0.4;

            pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x, y, z,
                    0.0, 0.01 + pRandom.nextDouble() * 0.01, 0.0);
        }

        for (int i = 0; i < 5 + pRandom.nextInt(10); i++) {
            double x = pPos.getX() + 0.3 + pRandom.nextDouble() * 0.3;
            double y = pPos.getY() + 0.1 + pRandom.nextDouble() * 0.5;
            double z = pPos.getZ() + 0.3 + pRandom.nextDouble() * 0.3;

            pLevel.addParticle(ParticleTypes.BUBBLE_POP,
                    x, y, z,
                    (pRandom.nextDouble() - 0.5) * 0.01,
                    0.03 + pRandom.nextDouble() * 0.02,
                    (pRandom.nextDouble() - 0.5) * 0.01);
        }

        for (int i = 0; i < 5 + pRandom.nextInt(4); i++) {
            double x = pPos.getX() + 0.3 + pRandom.nextDouble() * 0.5;
            double y = pPos.getY();
            double z = pPos.getZ() + 0.3 + pRandom.nextDouble() * 0.5;

            pLevel.addParticle(ParticleTypes.BUBBLE,
                    x, y, z,
                    (pRandom.nextDouble() - 0.5) * 0.01, // Небольшое горизонтальное движение
                    0.3 + pRandom.nextDouble() * 2,   // Скорость подъема пузырьков
                    (pRandom.nextDouble() - 0.5) * 0.01);
        }
    }

    private boolean isHeat(Level pLevel, BlockPos pPos) {
        BlockState blockBelow = pLevel.getBlockState(pPos);
        if (blockBelow.is(Blocks.CAMPFIRE) || blockBelow.is(Blocks.SOUL_CAMPFIRE)) {
            return blockBelow.getValue(CampfireBlock.LIT);
        }

        return false;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof CookingPotBlockEntity pot) {
                pot.dropInventory();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CookingPotBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof CookingPotBlockEntity pot) {
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), pot, pPos);
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
