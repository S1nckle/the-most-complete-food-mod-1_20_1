package net.shawdy.alacarte.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.shawdy.alacarte.client.screen.DietContainerScreen;

import java.util.function.Supplier;

public class S2CSendDietContainerDataPacket {
    private final byte[] VALUES;

    public S2CSendDietContainerDataPacket(byte... values) {
        this.VALUES = values;
    }

    public S2CSendDietContainerDataPacket(FriendlyByteBuf pBuffer) {
        this(pBuffer.readByteArray());
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeByteArray(this.VALUES);
    }

    public void handle(Supplier<NetworkEvent.Context> pContext) {
        pContext.get().enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof DietContainerScreen pScreen) {
                pScreen.updateDietValues(this.VALUES);
            }
        });
        pContext.get().setPacketHandled(true);
    }
}
