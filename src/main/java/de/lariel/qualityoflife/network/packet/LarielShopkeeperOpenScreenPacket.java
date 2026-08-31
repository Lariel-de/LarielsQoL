package de.lariel.qualityoflife.network.packet;

import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielShopkeeperScreen;
import de.lariel.qualityoflife.client.screen.services.LarielScreenService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.shopkeeper.CurrencyData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LarielShopkeeperOpenScreenPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielShopkeeperOpenScreenPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, p -> p.sync,
                    LarielShopkeeperOpenScreenPacket::new
            );
    public static final Type<LarielShopkeeperOpenScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "lariel_shopkeeper_open_screen_packet"));

    public LarielShopkeeperOpenScreenPacket(boolean sync) {
        super(sync);
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        var testItems = new ArrayList<LarielShopItem>();

        testItems.add(new LarielShopItem(new ShopItem(new ItemStack(Items.EMERALD), 0, 0),
                1, new CurrencyData(new ItemStack(Items.EMERALD))));
        testItems.add(new LarielShopItem(new ShopItem(PokeBallRegistry.POKE_BALL.get().getBallItem(), 1500.0, 500.0),
                10, new CurrencyData(new ItemStack(Items.DIRT, 10))));
        testItems.add(new LarielShopItem(new ShopItem(PokeBallRegistry.POKE_BALL.get().getBallItem(), 1500.0, 500.0)));

        LarielScreenService.openScreen(new LarielShopkeeperScreen(testItems, false));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
