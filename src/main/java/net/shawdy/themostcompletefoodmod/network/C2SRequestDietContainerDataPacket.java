package net.shawdy.themostcompletefoodmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.shawdy.themostcompletefoodmod.diet.playerDietContainer.IDietContainer;

import java.util.function.Supplier;

public class C2SRequestDietContainerDataPacket {
    public C2SRequestDietContainerDataPacket() {
    }

    public C2SRequestDietContainerDataPacket(FriendlyByteBuf pBuffer) {
        this();
    }

    public void encode(FriendlyByteBuf pBuffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            ServerPlayer pPlayer = pContext.get().getSender();
            byte[] data = ((IDietContainer) pPlayer.getFoodData()).the_most_complete_food_mod_1_20_1$getValuesFullness();
            NetworkHandler.sendToPlayer(pPlayer, new S2CSendDietContainerDataPacket(data));
        });
        pContext.get().setPacketHandled(true);
    }

}
