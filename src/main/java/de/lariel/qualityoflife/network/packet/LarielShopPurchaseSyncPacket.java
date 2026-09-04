package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.services.LarielShopPurchaseClientCache;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LarielShopPurchaseSyncPacket extends LarielPacketBase {
    public static final Type<LarielShopPurchaseSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "lariel_shop_purchase_sync_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielShopPurchaseSyncPacket> CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, p -> p.shopkeeperId,
                    UUIDUtil.STREAM_CODEC, p -> p.shopItemId,
                    ByteBufCodecs.VAR_INT, p -> p.purchasedToday,
                    LarielShopPurchaseSyncPacket::new
            );

    private final ResourceLocation shopkeeperId;
    private final UUID shopItemId;
    private final int purchasedToday;

    public LarielShopPurchaseSyncPacket(ResourceLocation shopkeeperId, UUID shopItemId, int purchasedToday) {
        super(true);
        this.shopkeeperId = shopkeeperId;
        this.shopItemId = shopItemId;
        this.purchasedToday = purchasedToday;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        Minecraft.getInstance().execute(() ->
                LarielShopPurchaseClientCache.setPurchasedToday(shopkeeperId, shopItemId, purchasedToday));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
