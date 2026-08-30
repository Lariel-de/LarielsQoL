package de.lariel.qualityoflife.shopkeeper;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;

public class LarielShopItem {

    private final ShopItem pixelmonItem;
    private final int customPrice;
    private final CurrencyData currency;

    public LarielShopItem(ShopItem item, int customPrice, CurrencyData currency) {
        this.pixelmonItem = item;
        this.customPrice = customPrice;
        this.currency = currency;
    }

    public ShopItem pixelmon() { return pixelmonItem; }
    public int price() { return customPrice; }
    public CurrencyData currency() { return currency; }
}