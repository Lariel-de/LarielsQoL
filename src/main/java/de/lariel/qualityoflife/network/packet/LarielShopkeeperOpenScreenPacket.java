package de.lariel.qualityoflife.network.packet;

import com.google.gson.reflect.TypeToken;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielShopkeeperScreen;
import de.lariel.qualityoflife.client.screen.services.LarielScreenService;
import de.lariel.qualityoflife.data.LarielShopItemJson;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LarielShopkeeperOpenScreenPacket extends LarielPacketBase {
    public static final Type<LarielShopkeeperOpenScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "lariel_shopkeeper_open_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielShopkeeperOpenScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.shopItemsJson),
                    buf -> new LarielShopkeeperOpenScreenPacket(buf.readUtf())
            );

    private final String shopItemsJson;

    public LarielShopkeeperOpenScreenPacket(String shopItemsJson) {
        super(true);
        this.shopItemsJson = shopItemsJson;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        List<LarielShopItemJson> dtoList =
                LarielsQoL.GSON.fromJson(shopItemsJson, new TypeToken<List<LarielShopItemJson>>(){}.getType());

        List<LarielShopItem> items = dtoList.stream()
                .map(LarielShopItemJson::toLarielShopItem)
                .toList();

        LarielScreenService.openScreen(new LarielShopkeeperScreen(items, false));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
