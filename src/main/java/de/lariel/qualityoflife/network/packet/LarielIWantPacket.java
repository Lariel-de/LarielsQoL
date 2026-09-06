package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.capture.LarielIWantService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class LarielIWantPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielIWantPacket> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, packet -> packet.targetUuid,
                    LarielIWantPacket::new
            );

    public static final Type<LarielIWantPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "i_want_packet"));

    private final UUID targetUuid;

    public LarielIWantPacket(UUID targetUuid) {
        super(true);
        this.targetUuid = targetUuid;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            LarielIWantService.getInstance().toggle(player, targetUuid);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
