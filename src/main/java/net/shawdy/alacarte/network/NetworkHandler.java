package net.shawdy.alacarte.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.shawdy.alacarte.ALaCarte;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ALaCarte.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(
                id++,
                S2CSendDietContainerDataPacket.class,
                S2CSendDietContainerDataPacket::encode,
                S2CSendDietContainerDataPacket::new,
                S2CSendDietContainerDataPacket::handle
        );
        INSTANCE.registerMessage(
                id++,
                C2SRequestDietContainerDataPacket.class,
                C2SRequestDietContainerDataPacket::encode,
                C2SRequestDietContainerDataPacket::new,
                C2SRequestDietContainerDataPacket::handle
        );
        INSTANCE.registerMessage(
                id++,
                C2SSendDrinkPacket.class,
                C2SSendDrinkPacket::encode,
                C2SSendDrinkPacket::new,
                C2SSendDrinkPacket::handle
        );
    }

    public static void sendToPlayer(ServerPlayer pPlayer, Object msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> pPlayer), msg);
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

}
