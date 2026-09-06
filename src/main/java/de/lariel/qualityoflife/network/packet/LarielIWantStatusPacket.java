package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.LarielIWantHud;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class LarielIWantStatusPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielIWantStatusPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(ArrayList::new, UUIDUtil.STREAM_CODEC), packet -> packet.wildTargets,
                    ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), packet -> java.util.Optional.ofNullable(packet.protectedPokemon),
                    LarielIWantStatusPacket::new
            );

    public static final Type<LarielIWantStatusPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "i_want_status_packet"));

    private final List<UUID> wildTargets;
    private final UUID protectedPokemon;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public LarielIWantStatusPacket(List<UUID> wildTargets, java.util.Optional<UUID> protectedPokemon) {
        this(wildTargets, protectedPokemon.orElse(null));
    }

    public LarielIWantStatusPacket(List<UUID> wildTargets, UUID protectedPokemon) {
        super(true);
        this.wildTargets = List.copyOf(wildTargets);
        this.protectedPokemon = protectedPokemon;
    }

    public static LarielIWantStatusPacket empty() {
        return new LarielIWantStatusPacket(List.of(), (UUID) null);
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        LarielIWantHud.update(wildTargets, protectedPokemon);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
