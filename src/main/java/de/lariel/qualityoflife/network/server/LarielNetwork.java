package de.lariel.qualityoflife.network.server;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.LarielOpenPokeBagPacket;
import de.lariel.qualityoflife.network.packet.LarielMintTradePacket;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID)
public class LarielNetwork {
    @SubscribeEvent
    public static void registerNetworking(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(LarielsQoL.MOD_ID);

        register(registrar, LarielOpenPokeBagPacket.TYPE, LarielOpenPokeBagPacket.CODEC, LarielPacketBase::handle);
        register2(registrar, LarielMintTradePacket.TYPE, LarielMintTradePacket.CODEC, LarielPacketBase::handle);
    }

    private static <T extends LarielPacketBase> void register2(PayloadRegistrar registrar,
                                                               CustomPacketPayload.Type<T> type,
                                                               StreamCodec<RegistryFriendlyByteBuf, T> codec,
                                                               IPayloadHandler<T> handler) {
        registrar.playToServer(type, codec, handler);
    }

    private static <T extends LarielPacketBase> void register(PayloadRegistrar registrar,
                                                              CustomPacketPayload.Type<T> type,
                                                              StreamCodec<? super ByteBuf, T> codec,
                                                              IPayloadHandler<T> handler) {
        registrar.playToServer(type, codec, handler);
    }
}