package de.lariel.qualityoflife.shopkeeper.utility;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.shopkeeper.data.CurrencyJson;
import de.lariel.qualityoflife.shopkeeper.data.ShopkeeperDefinition;
import de.lariel.qualityoflife.shopkeeper.data.TradeDefinition;
import de.lariel.qualityoflife.shopkeeper.CurrencyData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class LarielShopkeeperConverter {

    public static List<LarielShopItem> convertToLarielShopItems(ShopkeeperDefinition def,
                                                                HolderLookup.Provider registries) {
        return def.levels.entrySet().stream()
                .flatMap(entry -> {
                    int level = Integer.parseInt(entry.getKey());

                    return entry.getValue().stream()
                            .filter(trade -> Math.random() <= trade.chance)
                            .map(trade -> LarielShopkeeperConverter.convertTrade(trade, level, registries));
                })
                .toList();
    }

    private static LarielShopItem convertTrade(TradeDefinition trade, int level,
                                               HolderLookup.Provider registries) {
        if (trade == null)
            throw new IllegalArgumentException("Trade definition must not be null");
        if (trade.item == null || trade.item.isBlank())
            throw new IllegalArgumentException("Trade item must not be empty");
        if (trade.Xp < 0)
            throw new IllegalArgumentException("Trade Xp must not be negative");
        if (trade.maxSellCountPerDay < -1)
            throw new IllegalArgumentException("Trade maxSellCountPerDay must be -1 or greater");
        if (trade.amount < 1)
            throw new IllegalArgumentException("Trade amount must be greater than zero");

        var itemId = getItemId(trade.item);
        var item = LarielItemStackFactory.create(itemId, trade.nbt, registries);
        var currency = getCurrency(trade.currency, registries);
        var buyPrice = currency.type() == CurrencyType.POKEDOLLAR ? trade.price : 0;

        item.setCount(trade.amount);
        return new LarielShopItem(new ShopItem(item, buyPrice, 0), trade.price, currency, level,
                trade.Xp, trade.maxSellCountPerDay, trade.amount);
    }

    private static @NotNull ResourceLocation getItemId(String itemId) {
        return Optional.ofNullable(ResourceLocation.tryParse(itemId))
                .orElseThrow(() -> new IllegalArgumentException("Unknown item: " + itemId));
    }

    private static @NotNull CurrencyData getCurrency(CurrencyJson currency, HolderLookup.Provider registries) {
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
                yield new CurrencyData(LarielItemStackFactory.create(getItemId(itemId), currency.nbt, registries));
            }
            case CUSTOM -> {
                if (currency.key == null || currency.key.isBlank())
                    throw new IllegalArgumentException("Custom currency requires a key");
                yield new CurrencyData(currency.key, CurrencyType.CUSTOM);
            }
        };
    }
}