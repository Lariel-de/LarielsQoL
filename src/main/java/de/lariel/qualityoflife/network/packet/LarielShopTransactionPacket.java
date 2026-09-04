package de.lariel.qualityoflife.network.packet;

import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.reputation.LarielPlayerReputationStoreManager;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopkeeperStateManager;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopPurchaseStore;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LarielShopTransactionPacket extends LarielPacketBase {
    public static final StreamCodec<RegistryFriendlyByteBuf, LarielShopTransactionPacket> CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, p -> p.shopkeeperId,
                    UUIDUtil.STREAM_CODEC, p -> p.shopItemId,
                    ByteBufCodecs.INT, p -> p.quantity,
                    LarielShopTransactionPacket::new
            );

    public static final Type<LarielShopTransactionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "lariel_shop_transaction_packet"));

    private final ResourceLocation shopkeeperId;
    private final UUID shopItemId;
    private final int quantity;

    public LarielShopTransactionPacket(ResourceLocation shopkeeperId, UUID shopItemId, int quantity) {
        super(true);
        this.shopkeeperId = shopkeeperId;
        this.shopItemId = shopItemId;
        this.quantity = quantity;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) return;

        context.enqueueWork(() -> {
            if (quantity <= 0 || quantity > 2304) return;

            var shopItem = LarielShopkeeperStateManager.findItem(shopkeeperId, shopItemId).orElse(null);
            if (shopItem == null) return;
            long totalItems = (long) shopItem.getAmount() * quantity;
            if (totalItems > 2304 || !canFit(player.getInventory(), shopItem.getShopItem().itemStack(), (int) totalItems))
                return;
            if (!LarielShopPurchaseStore.canPurchase(player, shopkeeperId, shopItem, quantity))
                return;

            long totalPrice = (long) shopItem.getPrice() * quantity;
            if (totalPrice <= 0 || totalPrice > Integer.MAX_VALUE) return;

            if (!takeCurrency(player, shopItem, (int) totalPrice)) return;

            var purchased = shopItem.getShopItem().itemStack().copy();
            purchased.setCount((int) totalItems);
            if (!player.addItem(purchased)) {
                restoreCurrency(player, shopItem, (int) totalPrice);
                return;
            }
            player.inventoryMenu.broadcastChanges();

            LarielShopPurchaseStore.recordPurchase(player, shopkeeperId, shopItem, quantity);
            LarielNetwork.sendToClient(player, new LarielShopPurchaseSyncPacket(
                    shopkeeperId, shopItem.getShopItem().uuid(),
                    LarielShopPurchaseStore.getPurchasedToday(player, shopkeeperId, shopItem)));
            var earnedXp = Math.min(Integer.MAX_VALUE, (long) shopItem.getXp() * quantity);
            LarielPlayerReputationStoreManager.get(player).addXp(shopkeeperId, (int) earnedXp);
        });
    }

    private boolean takeCurrency(ServerPlayer player, LarielShopItem item, int amount) {
        return switch (item.getCurrencyData().type()) {
            case POKEDOLLAR -> {
                var bank = BankAccountProxy.getBankAccountNow(player);

                if (bank == null) yield false;

                yield bank.take(amount);
            }
            case SCOREBOARD -> {
                var objective = player.getScoreboard().getObjective(item.getCurrencyData().customKey());
                if (objective == null) yield false;
                var score = player.getScoreboard().getOrCreatePlayerScore(player, objective);
                if (score.get() < amount) yield false;
                score.set(score.get() - amount);
                yield true;
            }
            case ITEM -> removeItems(player.getInventory(), item.getCurrencyData().currencyItem(), amount);
            case CUSTOM -> false;
        };
    }

    private void restoreCurrency(ServerPlayer player, LarielShopItem item, int amount) {
        switch (item.getCurrencyData().type()) {
            case POKEDOLLAR -> {
                var bank = BankAccountProxy.getBankAccountNow(player);

                if (bank == null) return;

                bank.add(amount);
            }
            case SCOREBOARD -> {
                var objective = player.getScoreboard().getObjective(item.getCurrencyData().customKey());
                if (objective != null) {
                    var score = player.getScoreboard().getOrCreatePlayerScore(player, objective);
                    score.set(score.get() + amount);
                }
            }
            case ITEM -> player.getInventory().add(item.getCurrencyData().currencyItem().copyWithCount(amount));
            case CUSTOM -> {
            }
        }
    }

    private boolean removeItems(Inventory inventory, ItemStack currency, int amount) {
        if (currency == null || currency.isEmpty() || countItems(inventory, currency) < amount) return false;

        var remaining = amount;
        for (var stack : inventory.items) {
            if (!ItemStack.isSameItemSameComponents(stack, currency)) continue;
            var removed = Math.min(remaining, stack.getCount());
            stack.shrink(removed);
            remaining -= removed;
            if (remaining == 0) break;
        }
        return true;
    }

    private int countItems(Inventory inventory, ItemStack item) {
        return inventory.items.stream()
                .filter(stack -> ItemStack.isSameItemSameComponents(stack, item))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    private boolean canFit(Inventory inventory, ItemStack item, int amount) {
        var remaining = amount;
        for (var stack : inventory.items) {
            if (stack.isEmpty()) {
                remaining -= item.getMaxStackSize();
            } else if (ItemStack.isSameItemSameComponents(stack, item)) {
                remaining -= item.getMaxStackSize() - stack.getCount();
            }
            if (remaining <= 0) return true;
        }
        return false;
    }
}