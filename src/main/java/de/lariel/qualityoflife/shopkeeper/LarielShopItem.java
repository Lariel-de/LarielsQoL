package de.lariel.qualityoflife.shopkeeper;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import org.jetbrains.annotations.NotNull;

public class LarielShopItem {

    private final ShopItem shopItem;
    private final int customPrice;
    private final CurrencyData currencyData;

    public LarielShopItem(@NotNull ShopItem shopItem) {
        this(shopItem, 0, new CurrencyData());
    }

    public LarielShopItem(@NotNull ShopItem shopItem, int customPrice, @NotNull CurrencyData currencyData) {
        this.shopItem = getPixelmonItem(shopItem, currencyData);
        this.customPrice = customPrice;
        this.currencyData = currencyData;
    }

    public ShopItem shopItem() { return shopItem; }
    public int price() { return currencyData.type() == CurrencyType.POKEDOLLAR ? (int)shopItem.buyPrice() : customPrice; }
    public CurrencyData currencyData() { return currencyData; }

    private ShopItem getPixelmonItem(@NotNull ShopItem shopItem, @NotNull CurrencyData currencyData) {
        if (currencyData.type() == CurrencyType.POKEDOLLAR)
            return shopItem;

        return shopItem.withSellPrice(0).withBuyPrice(0);
    }
}