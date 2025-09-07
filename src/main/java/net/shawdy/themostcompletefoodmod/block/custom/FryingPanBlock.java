package net.shawdy.themostcompletefoodmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import net.shawdy.themostcompletefoodmod.block.entity.FryingPanBlockEntity;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FryingPanBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 14, 1.5, 14);

    public FryingPanBlock(Properties pProperties) {
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
        for (int i = 0; i < pRandom.nextInt(2); i++) { // Уменьшили количество дыма
            double x = pPos.getX() + 0.5 + (pRandom.nextDouble() - 0.5) * 0.4;
            double y = pPos.getY(); // Ниже начальная позиция
            double z = pPos.getZ() + 0.5 + (pRandom.nextDouble() - 0.5) * 0.4;

            // Меньшая скорость подъема для быстрого исчезновения
            pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x, y, z,
                    0.0, 0.01 + pRandom.nextDouble() * 0.01, 0.0); // Медленнее поднимается
        }
    }

    public boolean isHeat(Level pLevel, BlockPos pPos) {
        BlockState blockBelow = pLevel.getBlockState(pPos);
        if (blockBelow.is(Blocks.CAMPFIRE) || blockBelow.is(Blocks.SOUL_CAMPFIRE)) {
            return blockBelow.getValue(CampfireBlock.LIT);
        }

        return false;
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FryingPanBlockEntity(pPos, pState);
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof FryingPanBlockEntity pan) {
            ItemStackHandler pInventory = pan.getInventory();
            ItemStack handItem = pPlayer.getItemInHand(pHand);
            ItemStack panItem = pInventory.getStackInSlot(0);
            Optional<CampfireCookingRecipe> recipe = pan.getCookingRecipe(handItem);

            if (panItem.isEmpty()) {
                if (recipe.isPresent()) {
                    if (!pLevel.isClientSide) {
                        pan.placeCookingItem(handItem.split(handItem.getCount()), recipe.get().getCookingTime() / 4);
                        return InteractionResult.CONSUME;
                    }
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                if (pHand == InteractionHand.MAIN_HAND && handItem.isEmpty()) {
                    pPlayer.addItem(panItem.copy());
                    pInventory.setStackInSlot(0, ItemStack.EMPTY);
                    pan.setChanged();
                    return InteractionResult.CONSUME;
                } else if (ItemStack.isSameItemSameTags(handItem, panItem)) {
                    int amountToAdd = Math.min(handItem.getCount(), (panItem.getMaxStackSize() - panItem.getCount()));
                    if (amountToAdd > 0) {
                        panItem.grow(amountToAdd);
                        handItem.shrink(amountToAdd);
                        pan.setChanged();
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide && pBlockEntityType == ModBlockEntities.FRYING_PAN_BE.get()) {
            return (level, pos, state, blockEntity) -> {
                FryingPanBlockEntity pan = (FryingPanBlockEntity) blockEntity;
                if (isHeat(level, pos.below())) {
                    pan.cookTick(level, pos, state, pan);
                } else {
                    pan.waitTick(level, pos, state, pan);
                }
            };
        }
        return null;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof FryingPanBlockEntity pan) {
                pan.dropInventory();
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }
}
