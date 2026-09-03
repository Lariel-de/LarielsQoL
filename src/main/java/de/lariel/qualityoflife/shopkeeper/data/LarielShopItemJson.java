package de.lariel.qualityoflife.shopkeeper.data;

import com.google.gson.JsonElement;
import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.shopkeeper.CurrencyData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import de.lariel.qualityoflife.shopkeeper.utility.LarielItemStackFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record LarielShopItemJson(String shopItemId, String itemId, JsonElement itemNbt, int price, String currencyType,
                                 String currencyItem, JsonElement currencyNbt, String currencyKey,
                                 int level, int xp, int maxSellCountPerDay) {
    public LarielShopItem toLarielShopItem() {
        var stack = LarielItemStackFactory.create(ResourceLocation.parse(itemId), itemNbt);

        var currency = switch (CurrencyType.valueOf(currencyType)) {
            case POKEDOLLAR -> new CurrencyData();
            case ITEM ->
                    new CurrencyData(LarielItemStackFactory.create(ResourceLocation.parse(currencyItem), currencyNbt));
            case SCOREBOARD -> new CurrencyData(currencyKey);
            case CUSTOM -> new CurrencyData(currencyKey, CurrencyType.CUSTOM);
        };

        var pixelmonItem = new ShopItem(shopItemId == null ? UUID.randomUUID() : UUID.fromString(shopItemId),
                stack, price, 0);

        return new LarielShopItem(pixelmonItem, price, currency, level, xp, maxSellCountPerDay);
    }
}
