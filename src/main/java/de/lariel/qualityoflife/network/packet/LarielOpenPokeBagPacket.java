package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.utility.LarielPokeBagHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class LarielOpenPokeBagPacket extends LarielPacketBase {
    // Keep that constructor, else I have to implement singleton
    // because StreamCoded.unit expects the absolute same instance
    public LarielOpenPokeBagPacket(boolean sync) {
        super(sync);
    }

    public static final StreamCodec<ByteBuf, LarielOpenPokeBagPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, p -> true,
            LarielOpenPokeBagPacket::new);

    public static final Type<LarielOpenPokeBagPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "hotkey_packet"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();

        LarielPokeBagHelper.openFirstPokeBag(player);
    }
}



