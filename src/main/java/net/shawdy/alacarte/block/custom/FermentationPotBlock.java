package net.shawdy.alacarte.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.shawdy.alacarte.block.entity.FermentationPotBlockEntity;
import net.shawdy.alacarte.block.entity.FryingPanBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FermentationPotBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty OPENED = BooleanProperty.create("opened");
    public static BooleanProperty FILLED = BooleanProperty.create("filled");

    private final VoxelShape BOTTOM = Block.box(1, 0, 1, 15, 2, 15);
    private final VoxelShape SIDE_1 = Block.box(1, 1, 1, 2, 16, 15);
    private final VoxelShape SIDE_2 = Block.box(2, 1, 1, 15, 16, 2);
    private final VoxelShape SIDE_3 = Block.box(2, 1, 14, 15, 16, 15);
    private final VoxelShape SIDE_4 = Block.box(14, 1, 2, 15, 16, 14);
    private final VoxelShape THROAT_1 = Block.box(2, 16, 2, 14, 17, 3);
    private final VoxelShape THROAT_2 = Block.box(2, 16, 3, 3, 17, 14);
    private final VoxelShape THROAT_3 = Block.box(13, 16, 3, 14, 17, 14);
    private final VoxelShape THROAT_4 = Block.box(2, 16, 13, 14, 17, 14);
    private final VoxelShape HAT = Block.box(2, 17, 2, 14, 18, 14);
    private final VoxelShape BODY = Shapes.or(BOTTOM, SIDE_1, SIDE_2, SIDE_3, SIDE_4, THROAT_1, THROAT_2, THROAT_3, THROAT_4);


    public FermentationPotBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPENED, Boolean.TRUE)
                .setValue(FILLED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(OPENED) ? BODY : Shapes.or(BODY, HAT);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(OPENED);
        pBuilder.add(FILLED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(OPENED, Boolean.TRUE)
                .setValue(FILLED, Boolean.FALSE);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            if (pHand != InteractionHand.OFF_HAND) {
                if (pPlayer.isCrouching() || pPlayer.getPose() == Pose.SWIMMING) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(OPENED, !pState.getValue(OPENED)));
                    pLevel.playSound(null, pPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 0.7f);
                    return InteractionResult.SUCCESS;
                }
            }

            if (pState.getValue(OPENED)) {
                if (pPlayer.getItemInHand(pHand).is(Items.BUCKET) && pState.getValue(FILLED)) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(FILLED, !pState.getValue(FILLED)));
                    if (!pPlayer.isCreative()) pPlayer.setItemInHand(pHand, new ItemStack(Items.WATER_BUCKET));
                    pLevel.playSound(null, pPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.gameEvent(null, GameEvent.FLUID_PICKUP, pPos);
                    return InteractionResult.SUCCESS;

                } else if (pPlayer.getItemInHand(pHand).is(Items.WATER_BUCKET) && !pState.getValue(FILLED)) {
                    if (!pPlayer.isCreative()) pPlayer.setItemInHand(pHand, new ItemStack(Items.BUCKET));
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(FILLED, !pState.getValue(FILLED)));
                    pLevel.playSound(null, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.gameEvent(null, GameEvent.FLUID_PLACE, pPos);
                    return InteractionResult.SUCCESS;
                } else {
                    BlockEntity be = pLevel.getBlockEntity(pPos);
                    if (be instanceof FermentationPotBlockEntity pot) {
                        ItemStack pStack = pPlayer.getItemInHand(pHand);

                        if (!pStack.isEmpty() && pot.canPlaceItem()) {
                            pot.placeItem(pStack.split(1));
                        } else if (pStack.isEmpty() && pot.hasItem()) {
                            pPlayer.getInventory().add(pot.removeItem());
                        }
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof FermentationPotBlockEntity pot) {
                pot.dropInventory();
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FermentationPotBlockEntity(pPos, pState);
    }
}



