package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielBetterBreedingScreen;
import de.lariel.qualityoflife.client.screen.services.LarielScreenService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class LarielBetterBreedingOpenScreenPacket extends LarielPacketBase {
    public LarielBetterBreedingOpenScreenPacket(boolean sync) {
        super(sync);
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        LarielScreenService.openScreen(new LarielBetterBreedingScreen());
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielBetterBreedingOpenScreenPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, p -> p.sync,
                    LarielBetterBreedingOpenScreenPacket::new
            );

    public static final Type<LarielBetterBreedingOpenScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "better_breeding_open_screen_packet"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
