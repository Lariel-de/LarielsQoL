package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielBetterBreedingScreen;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class LarielBetterBreedingStatusPacket extends LarielPacketBase {
    private final String message;
    private final boolean success;

    public LarielBetterBreedingStatusPacket(String message, boolean success) {
        super(true);

        this.message = message;
        this.success = success;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof LarielBetterBreedingScreen screen) {
                screen.showStatus(Component.translatable(message), 100, super.sync); // 4 Sekunden
            }
        });
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielBetterBreedingStatusPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, p -> p.message,
                    ByteBufCodecs.BOOL, p -> p.success,
                    LarielBetterBreedingStatusPacket::new
            );

    public static final Type<LarielBetterBreedingStatusPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "better_breeding_status_packet"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
