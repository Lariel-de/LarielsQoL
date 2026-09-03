package de.lariel.qualityoflife.shopkeeper;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import org.jetbrains.annotations.NotNull;

public class LarielShopItem {

    private final ShopItem shopItem;
    private final int customPrice;
    private final CurrencyData currencyData;
    private final int minLevel;
    private final int xp;
    private final int maxSellCountPerDay;

    public LarielShopItem(@NotNull ShopItem shopItem, int customPrice, @NotNull CurrencyData currencyData, int minLevel,
                          int xp, int maxSellCountPerDay) {
        this.minLevel = minLevel;
        this.shopItem = getPixelmonItem(shopItem, currencyData);
        this.customPrice = customPrice;
        this.currencyData = currencyData;
        this.xp = xp;
        this.maxSellCountPerDay = maxSellCountPerDay;
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

    public int getXp() {
        return xp;
    }

    public int getMaxSellCountPerDay() {
        return maxSellCountPerDay;
    }

    private ShopItem getPixelmonItem(@NotNull ShopItem shopItem, @NotNull CurrencyData currencyData) {
        if (currencyData.type() == CurrencyType.POKEDOLLAR)
            return shopItem;

        return new ShopItem(shopItem.uuid(), shopItem.itemStack(), 0, 0);
    }
}