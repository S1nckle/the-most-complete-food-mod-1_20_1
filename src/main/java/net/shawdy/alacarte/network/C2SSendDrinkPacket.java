package net.shawdy.alacarte.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.shawdy.alacarte.diet.playerDietContainer.IDietContainer;

import java.util.function.Supplier;

public class C2SSendDrinkPacket {
    private final BlockPos POSITION;

    public C2SSendDrinkPacket(FriendlyByteBuf pBuffer) {
        this(pBuffer.readBlockPos());
    }

    public C2SSendDrinkPacket(BlockPos pos) {
        POSITION = pos;
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeBlockPos(POSITION);
    }

    public void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            ServerPlayer pPlayer = pContext.get().getSender();
            Level level = pPlayer.level();
            BlockState block = level.getBlockState(POSITION);
            if(block.is(Blocks.WATER_CAULDRON)) {
                int waterLevel = block.getValue(LayeredCauldronBlock.LEVEL);
                if (waterLevel > 1) {
                    level.setBlock(POSITION, block.setValue(LayeredCauldronBlock.LEVEL, waterLevel - 1), 3);
                } else {
                    level.setBlock(POSITION, Blocks.CAULDRON.defaultBlockState(), 3);
                }
            }
            if (pPlayer != null) {
                ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("water", 5.0f);
                if(block.is(Blocks.WATER_CAULDRON))
                    ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod$increaseValue("water", 5.0f);
                pPlayer.swing(InteractionHand.MAIN_HAND);
                pPlayer.level().playSound(
                        null,
                        pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        SoundEvents.GENERIC_DRINK,
                        SoundSource.PLAYERS,
                        0.2f,
                        1.0f
                );
                ((ServerLevel) pPlayer.level()).sendParticles(
                        ParticleTypes.SPLASH,
                        pPlayer.getX(), pPlayer.getY() + 0.9, pPlayer.getZ(),
                        8, // Количество
                        0.05, 0, 0.05, // Разброс
                        0.2 // Скорость
                );
            }
            pContext.get().setPacketHandled(true);
        });
    }
}
