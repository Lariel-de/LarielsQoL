package de.lariel.qualityoflife.network.server;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.*;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID)
public class LarielNetwork {
    @SubscribeEvent
    public static void registerNetworking(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(LarielsQoL.MOD_ID);

        // Client → Server
        registerToServer(registrar, LarielOpenPokeBagPacket.TYPE, LarielOpenPokeBagPacket.CODEC);
        registerToServer(registrar, LarielMintTraderPacket.TYPE, LarielMintTraderPacket.CODEC);
        registerToServer(registrar, LarielBetterBreedingApplyPacket.TYPE, LarielBetterBreedingApplyPacket.CODEC);
        registerToServer(registrar, LarielShopTransactionPacket.TYPE, LarielShopTransactionPacket.CODEC);

        // Server → Client
        registerToClient(registrar, LarielMintTraderStatusPacket.TYPE, LarielMintTraderStatusPacket.CODEC);
        registerToClient(registrar, LarielBetterBreedingOpenScreenPacket.TYPE, LarielBetterBreedingOpenScreenPacket.CODEC);
        registerToClient(registrar, LarielBetterBreedingStatusPacket.TYPE, LarielBetterBreedingStatusPacket.CODEC);
        registerToClient(registrar, LarielShopkeeperOpenScreenPacket.TYPE, LarielShopkeeperOpenScreenPacket.CODEC);
        registerToClient(registrar, LarielShopPurchaseSyncPacket.TYPE, LarielShopPurchaseSyncPacket.CODEC);
    }

    private static <T extends LarielPacketBase> void registerToServer(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToServer(type, codec, LarielPacketBase::handle);
    }

    private static <T extends LarielPacketBase> void registerToClient(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToClient(type, codec, LarielPacketBase::handle);
    }

    public static void sendToClient(@NotNull ServerPlayer player, @NotNull LarielPacketBase packet) {
        player.connection.send(packet);
    }

    public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        PacketDistributor.sendToServer(payload, payloads);
    }
}