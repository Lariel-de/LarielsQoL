package de.lariel.qualityoflife.network.packet.base;

import com.pixelmonmod.pixelmon.Pixelmon;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.LarielOpenPokeBagPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class LarielPacketBase implements CustomPacketPayload {
    private final boolean _sync;

    protected LarielPacketBase(boolean sync) {
        _sync = sync;
    }

    public static final Type<LarielOpenPokeBagPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "hotkey_packet"));

    public void handle(IPayloadContext context) {
        if (_sync) {
            context.enqueueWork(() -> {
                try {
                    this.handlePacket(context);
                } catch (Exception e) {
                    Pixelmon.LOGGER.error("Error handling packet", e);
                }

            });

            return;
        }

        this.handlePacket(context);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    protected abstract void handlePacket(IPayloadContext context);
}
