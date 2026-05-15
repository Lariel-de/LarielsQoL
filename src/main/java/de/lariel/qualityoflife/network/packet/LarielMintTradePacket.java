package de.lariel.qualityoflife.network.packet;

import com.pixelmonmod.pixelmon.init.registry.ItemRegistration;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.gui.MintTraderMenu;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LarielMintTradePacket extends LarielPacketBase {
    private final Item selectedMint;

    private static final TagKey<Item> MINT_TAG =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("larielsqol", "mints"));

    public static final List<Item> DESIRED_MINTS = List.of(
            ItemRegistration.MINT_JOLLY.get(),
            ItemRegistration.MINT_TIMID.get(),
            ItemRegistration.MINT_MODEST.get(),
            ItemRegistration.MINT_ADAMANT.get()
    );

    public LarielMintTradePacket(boolean sync, Item selectedMint) {
        super(sync);

        this.selectedMint = selectedMint;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielMintTradePacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, p -> p.sync,
                    ByteBufCodecs.registry(Registries.ITEM), p -> p.selectedMint,
                    LarielMintTradePacket::new
            );

    public static final Type<LarielMintTradePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "mint_trader_packet"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            MintTraderMenu menu = (MintTraderMenu) player.containerMenu;

            Container c = menu.getContainer();

            int count = 0;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = c.getItem(i);
                if (isMint(stack)) {
                    count += stack.getCount();
                }
            }

            if (count < 9) {
                player.sendSystemMessage(Component.literal("Du brauchst 9 Minzen!"));
                return;
            }

            removeMints(c, 9);

            Item reward = getMintByName(selectedMint);
            player.getInventory().add(new ItemStack(reward));

            player.sendSystemMessage(Component.literal("Danke für den Tausch!"));
        });
    }

    private Item getMintByName(Item selectedMint) {
        return DESIRED_MINTS.get(DESIRED_MINTS.indexOf(selectedMint));
    }

    private void removeMints(Container c, int amount) {
        int remaining = amount;

        for (int i = 0; i < 9 && remaining > 0; i++) {
            ItemStack stack = c.getItem(i);

            if (isMint(stack)) {
                int remove = Math.min(stack.getCount(), remaining);
                stack.shrink(remove);
                remaining -= remove;

                if (stack.getCount() <= 0) {
                    c.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    private boolean isMint(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.is(MINT_TAG);
    }
}
