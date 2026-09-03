package de.lariel.qualityoflife.shopkeeper.data;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.shopkeeper.CurrencyData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record LarielShopItemJson(String shopItemId, String itemId, int price, String currencyType, String currencyItem, String currencyKey,
                                 int level, int xp, int maxSellCountPerDay) {
    public LarielShopItem toLarielShopItem() {
        var stack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId)));

        var currency = switch (CurrencyType.valueOf(currencyType)) {
            case POKEDOLLAR -> new CurrencyData();
            case ITEM ->
                    new CurrencyData(new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(currencyItem))));
            case SCOREBOARD -> new CurrencyData(currencyKey);
            case CUSTOM -> new CurrencyData(currencyKey, CurrencyType.CUSTOM);
        };

        var pixelmonItem = new ShopItem(shopItemId == null ? UUID.randomUUID() : UUID.fromString(shopItemId),
                stack, price, 0);

        return new LarielShopItem(pixelmonItem, price, currency, level, xp, maxSellCountPerDay);
    }
}
