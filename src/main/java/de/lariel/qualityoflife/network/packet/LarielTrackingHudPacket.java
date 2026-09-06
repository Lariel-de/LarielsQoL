package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.LarielTrackingHud;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class LarielTrackingHudPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielTrackingHudPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, packet -> packet.active,
                    ByteBufCodecs.FLOAT, packet -> packet.relativeAngle,
                    ByteBufCodecs.VAR_INT, packet -> packet.distance,
                    LarielTrackingHudPacket::new
            );

    public static final Type<LarielTrackingHudPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "tracking_hud_packet"));

    private final boolean active;
    private final float relativeAngle;
    private final int distance;

    public LarielTrackingHudPacket(boolean active, float relativeAngle, int distance) {
        super(true);
        this.active = active;
        this.relativeAngle = relativeAngle;
        this.distance = distance;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        LarielTrackingHud.update(active, relativeAngle, distance);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
