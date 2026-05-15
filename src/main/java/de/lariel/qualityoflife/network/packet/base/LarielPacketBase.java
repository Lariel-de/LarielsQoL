package de.lariel.qualityoflife.network.packet.base;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class LarielPacketBase implements CustomPacketPayload {
    protected final boolean sync;

    protected LarielPacketBase(boolean sync) {
        this.sync = sync;
    }

    public void handle(IPayloadContext context) {
        if (sync) {
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

    protected abstract void handlePacket(IPayloadContext context);
}
