package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.utility.LarielPokeBagHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

    @Override
    protected void handlePacket(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();

        LarielPokeBagHelper.openFirstPokeBag(player);
    }
}



