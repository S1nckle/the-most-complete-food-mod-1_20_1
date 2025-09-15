package net.shawdy.alacarte.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.shawdy.alacarte.block.entity.CuttingBoardBlockEntity;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlock extends Block implements EntityBlock{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE_NORTH = Block.box(1, 0, 2, 15, 1, 14);
    private static final VoxelShape SHAPE_EAST = Block.box(2, 0, 1, 14, 1, 15);

    public CuttingBoardBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(CuttingBoardBlock.FACING);
        if (facing == Direction.WEST || facing == Direction.EAST) {
            return SHAPE_EAST;
        }
        return SHAPE_NORTH;
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
        return below.isFaceSturdy(pLevel, pPos.below(), Direction.UP, SupportType.FULL) || below.is(Blocks.CAMPFIRE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CuttingBoardBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof CuttingBoardBlockEntity board) {
            SimpleContainer pInventory = board.getInventory();
            ItemStack handItem = pPlayer.getItemInHand(pHand);
            ItemStack offhandItem = pPlayer.getOffhandItem();
            ItemStack boardItem = pInventory.getItem(0);

            if (boardItem.isEmpty()) {
                if (!handItem.isEmpty()) {
                    pInventory.setItem(0, handItem.split(1));
                    return InteractionResult.SUCCESS;
                } else if (!offhandItem.isEmpty()) {
                    pInventory.setItem(0, offhandItem.split(1));
                    return InteractionResult.SUCCESS;
                }
            }
            if (!boardItem.isEmpty() && handItem.isEmpty()) {
                pPlayer.addItem(boardItem.copy());
                pInventory.setItem(0, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof CuttingBoardBlockEntity board) {
                board.dropInventory();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
