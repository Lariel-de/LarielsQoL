package de.lariel.qualityoflife.shopkeeper.utility;

import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class LarielShopPurchaseStore {
    private static final String ROOT_KEY = "lariel_shopkeeper_purchases";
    private static final String DAY_KEY = "day";
    private static final String COUNT_KEY = "count";

    private LarielShopPurchaseStore() {
    }

    public static boolean canPurchase(ServerPlayer player, ResourceLocation shopkeeperId, LarielShopItem item, int quantity) {
        int limit = item.getMaxSellCountPerDay();
        if (limit < 0) return true;

        return getPurchasedToday(player, shopkeeperId, item) + 1 <= limit;
    }

    public static void recordPurchase(ServerPlayer player, ResourceLocation shopkeeperId, LarielShopItem item, int quantity) {
        if (item.getMaxSellCountPerDay() < 0) return;

        var purchases = getPurchases(player);
        var purchase = getPurchase(purchases, shopkeeperId, item);
        purchase.putLong(DAY_KEY, currentDay(player));
        purchase.putInt(COUNT_KEY, getPurchasedToday(purchase, player) + 1);
        savePurchases(player, purchases);
    }

    public static int getPurchasedToday(Player player, ResourceLocation shopkeeperId, LarielShopItem item) {
        return getPurchasedToday(getPurchase(getPurchases(player), shopkeeperId, item), player);
    }

    private static int getPurchasedToday(CompoundTag purchase, Player player) {
        return purchase.getLong(DAY_KEY) == currentDay(player) ? purchase.getInt(COUNT_KEY) : 0;
    }

    private static CompoundTag getPurchases(Player player) {
        return player.getPersistentData().getCompound("PlayerPersisted").getCompound(ROOT_KEY);
    }

    private static CompoundTag getPurchase(CompoundTag purchases, ResourceLocation shopkeeperId, LarielShopItem item) {
        var shopkeeperKey = shopkeeperId.toString();
        var itemKey = item.getShopItem().uuid().toString();

        CompoundTag shopkeeper = purchases.contains(shopkeeperKey)
                ? purchases.getCompound(shopkeeperKey)
                : new CompoundTag();

        CompoundTag purchase = shopkeeper.contains(itemKey)
                ? shopkeeper.getCompound(itemKey)
                : new CompoundTag();

        shopkeeper.put(itemKey, purchase);
        purchases.put(shopkeeperKey, shopkeeper);

        return purchase;
    }

    private static void savePurchases(ServerPlayer player, CompoundTag purchases) {
        var persisted = player.getPersistentData().getCompound("PlayerPersisted");
        persisted.put(ROOT_KEY, purchases);
        player.getPersistentData().put("PlayerPersisted", persisted);
    }

    private static long currentDay(Player player) {
        return player.level().getDayTime() / 24000L;
    }
}
