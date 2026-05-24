package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielBreedingApplyService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LarielBetterBreedingApplyPacket extends LarielPacketBase {

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielBetterBreedingApplyPacket> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, p -> p.eggUUID,
                    ByteBufCodecs.STRING_UTF8, p -> p.form,
                    ByteBufCodecs.STRING_UTF8, p -> p.palette,
                    ByteBufCodecs.STRING_UTF8, p -> p.gender,
                    LarielBetterBreedingApplyPacket::new
            );
    public static final Type<LarielBetterBreedingApplyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("larielsqol", "better_breeding_packet"));
    private final UUID eggUUID;
    private final String form;
    private final String palette;
    private final String gender;

    public LarielBetterBreedingApplyPacket(UUID eggUUID, String form, String palette, String gender) {
        super(true);
        this.eggUUID = eggUUID;
        this.form = form;
        this.palette = palette;
        this.gender = gender;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) return;

        context.enqueueWork(() ->
                LarielBreedingApplyService.apply(player, eggUUID, form, palette, gender)
        );
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}