package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielMintTraderScreen;
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

public class LarielMintTraderStatusPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielMintTraderStatusPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, p -> p.sync,
                    ByteBufCodecs.STRING_UTF8, p -> p.translationKey,
                    LarielMintTraderStatusPacket::new
            );
    public static final Type<LarielMintTraderStatusPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "mint_trader_status_packet"));
    private final String translationKey;

    protected LarielMintTraderStatusPacket(boolean success, String translationKey) {
        super(success);

        this.translationKey = translationKey;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof LarielMintTraderScreen screen) {
                screen.showStatusMessage(Component.translatable(translationKey), 100, super.sync); // 3 Sekunden
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
