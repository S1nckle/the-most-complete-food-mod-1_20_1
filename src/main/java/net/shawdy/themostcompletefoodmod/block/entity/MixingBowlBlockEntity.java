package net.shawdy.themostcompletefoodmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.shawdy.themostcompletefoodmod.init.ModBlockEntities;

public class MixingBowlBlockEntity extends BlockEntity {
    public MixingBowlBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MIXING_BOWL_BE.get(), pPos, pBlockState);
    }
}
