package de.lariel.qualityoflife.network.packet;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielShopkeeperScreen;
import de.lariel.qualityoflife.client.screen.services.LarielScreenService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopkeeperSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LarielShopkeeperOpenScreenPacket extends LarielPacketBase {
    public static final Type<LarielShopkeeperOpenScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "lariel_shopkeeper_open_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielShopkeeperOpenScreenPacket> CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, p -> p.shopkeeperId,
                    ByteBufCodecs.STRING_UTF8, p -> p.shopItemsJson,
                    LarielShopkeeperOpenScreenPacket::new
            );

    private final String shopItemsJson;
    private final ResourceLocation shopkeeperId;

    public LarielShopkeeperOpenScreenPacket(ResourceLocation shopkeeperId, String shopItemsJson) {
        super(true);
        this.shopkeeperId = shopkeeperId;
        this.shopItemsJson = shopItemsJson;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        List<LarielShopItem> items = LarielShopkeeperSerializer.deserialize(shopItemsJson, level.registryAccess());
        LarielScreenService.openScreen(new LarielShopkeeperScreen(shopkeeperId, items, false));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
