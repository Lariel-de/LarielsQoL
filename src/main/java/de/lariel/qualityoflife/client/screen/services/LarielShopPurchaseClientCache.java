package de.lariel.qualityoflife.client.screen.services;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LarielShopPurchaseClientCache {
    private static final Map<String, Integer> PURCHASES = new HashMap<>();

    private LarielShopPurchaseClientCache() {
    }

    public static void setPurchasedToday(ResourceLocation shopkeeperId, UUID shopItemId, int count) {
        PURCHASES.put(key(shopkeeperId, shopItemId), count);
    }

    public static int getPurchasedToday(ResourceLocation shopkeeperId, UUID shopItemId) {
        return PURCHASES.getOrDefault(key(shopkeeperId, shopItemId), 0);
    }

    private static String key(ResourceLocation shopkeeperId, UUID shopItemId) {
        return shopkeeperId + "|" + shopItemId;
    }
}
