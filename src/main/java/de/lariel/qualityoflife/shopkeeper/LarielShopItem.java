package de.lariel.qualityoflife.shopkeeper;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import org.jetbrains.annotations.NotNull;

public class LarielShopItem {

    private final ShopItem shopItem;
    private final int customPrice;
    private final CurrencyData currencyData;
    private final int minLevel;

    public LarielShopItem(@NotNull ShopItem shopItem, int customPrice, @NotNull CurrencyData currencyData, int minLevel) {
        this.minLevel = minLevel;
        this.shopItem = getPixelmonItem(shopItem, currencyData);
        this.customPrice = customPrice;
        this.currencyData = currencyData;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    public int getPrice() {
        return currencyData.type() == CurrencyType.POKEDOLLAR ? (int) shopItem.buyPrice() : customPrice;
    }

    public CurrencyData getCurrencyData() {
        return currencyData;
    }

    public int getMinLevel() {
        return minLevel;
    }

    private ShopItem getPixelmonItem(@NotNull ShopItem shopItem, @NotNull CurrencyData currencyData) {
        if (currencyData.type() == CurrencyType.POKEDOLLAR)
            return shopItem;

        return shopItem.withSellPrice(0).withBuyPrice(0);
    }
}