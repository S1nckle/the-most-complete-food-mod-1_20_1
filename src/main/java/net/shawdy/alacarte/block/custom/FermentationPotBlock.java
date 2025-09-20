package net.shawdy.alacarte.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FermentationPotBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty OPENED = BooleanProperty.create("opened");

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
                .setValue(OPENED, Boolean.TRUE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return OPENED.getValue("opened").isPresent() && OPENED.getValue("opened").get() ? Shapes.or(BODY, HAT) : BODY;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(OPENED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(OPENED, Boolean.TRUE);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide() && pHand != InteractionHand.OFF_HAND) {
            if (pPlayer.isCrouching()) {
                BlockState newState = pState.setValue(OPENED, !pState.getValue(OPENED));
                pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
                System.out.println("Opened");
                System.out.println(pState.getValue(OPENED));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
