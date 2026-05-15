package de.lariel.qualityoflife.network.server;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.LarielMintTraderPacket;
import de.lariel.qualityoflife.network.packet.LarielMintTraderStatusPacket;
import de.lariel.qualityoflife.network.packet.LarielOpenPokeBagPacket;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID)
public class LarielNetwork {
    @SubscribeEvent
    public static void registerNetworking(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(LarielsQoL.MOD_ID);

        // Client → Server
        registerToServer(registrar, LarielOpenPokeBagPacket.TYPE, LarielOpenPokeBagPacket.CODEC);
        registerToServer(registrar, LarielMintTraderPacket.TYPE, LarielMintTraderPacket.CODEC);

        // Server → Client
        registerToClient(registrar, LarielMintTraderStatusPacket.TYPE, LarielMintTraderStatusPacket.CODEC);
    }

    private static <T extends LarielPacketBase> void registerToServer(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToServer(type, codec, LarielPacketBase::handle);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends LarielPacketBase> void registerToClient(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToClient(type, codec, LarielPacketBase::handle);
    }
}