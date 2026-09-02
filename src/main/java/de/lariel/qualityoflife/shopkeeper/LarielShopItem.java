package de.lariel.qualityoflife.shopkeeper;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.data.CurrencyJson;
import de.lariel.qualityoflife.data.LarielShopItemJson;
import de.lariel.qualityoflife.data.TradeDefinition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

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

    public static LarielShopItem fromJson(TradeDefinition trade, int level) {
        if (trade == null)
            throw new IllegalArgumentException("Trade definition must not be null");
        if (trade.item == null || trade.item.isBlank())
            throw new IllegalArgumentException("Trade item must not be empty");

        var item = getItem(trade.item);
        var currency = getCurrency(trade.currency);
        var buyPrice = currency.type() == CurrencyType.POKEDOLLAR ? trade.price : 0;

        return new LarielShopItem(
                new ShopItem(new ItemStack(item), buyPrice, 0),
                trade.price,
                currency
        );
    }

    public LarielShopItemJson toJsonDto() {
        return new LarielShopItemJson(
                BuiltInRegistries.ITEM.getKey(shopItem.itemStack().getItem()).toString(),
                customPrice,
                currencyData.type().name(),
                currencyData.currencyItem() != null ?
                        BuiltInRegistries.ITEM.getKey(currencyData.currencyItem().getItem()).toString() : null,
                currencyData.customKey(),
                // falls du Level brauchst
                0
        );
    }

    public ShopItem shopItem() { return shopItem; }
    public int price() { return currencyData.type() == CurrencyType.POKEDOLLAR ? (int)shopItem.buyPrice() : customPrice; }
    public CurrencyData currencyData() { return currencyData; }

    private ShopItem getPixelmonItem(@NotNull ShopItem shopItem, @NotNull CurrencyData currencyData) {
        if (currencyData.type() == CurrencyType.POKEDOLLAR)
            return shopItem;

        return shopItem.withSellPrice(0).withBuyPrice(0);
    }

    private static CurrencyData getCurrency(CurrencyJson currency) {
        if (currency == null || currency.type == null || currency.type.isBlank())
            return new CurrencyData();

        CurrencyType type;
        try {
            type = CurrencyType.valueOf(currency.type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown currency type: " + currency.type, exception);
        }

        return switch (type) {
            case POKEDOLLAR -> new CurrencyData();
            case SCOREBOARD -> {
                if (currency.objective == null || currency.objective.isBlank())
                    throw new IllegalArgumentException("Scoreboard currency requires an objective");
                yield new CurrencyData(currency.objective);
            }
            case ITEM -> {
                var itemId = currency.item != null && !currency.item.isBlank()
                        ? currency.item
                        : currency.key;
                if (itemId == null || itemId.isBlank())
                    throw new IllegalArgumentException("Item currency requires an item");
                yield new CurrencyData(new ItemStack(getItem(itemId)));
            }
            case CUSTOM -> {
                if (currency.key == null || currency.key.isBlank())
                    throw new IllegalArgumentException("Custom currency requires a key");
                yield new CurrencyData(currency.key, CurrencyType.CUSTOM);
            }
        };
    }

    private static Item getItem(String itemId) {
        var id = ResourceLocation.tryParse(itemId);
        if (id == null)
            throw new IllegalArgumentException("Invalid item identifier: " + itemId);

        var item = BuiltInRegistries.ITEM.get(id);
        if (item == net.minecraft.world.item.Items.AIR)
            throw new IllegalArgumentException("Unknown item: " + itemId);

        return item;
    }
}