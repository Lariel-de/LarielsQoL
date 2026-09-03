package de.lariel.qualityoflife.shopkeeper.utility;

import com.pixelmonmod.pixelmon.api.shop.ShopItem;
import de.lariel.qualityoflife.shopkeeper.data.CurrencyJson;
import de.lariel.qualityoflife.shopkeeper.data.ShopkeeperDefinition;
import de.lariel.qualityoflife.shopkeeper.data.TradeDefinition;
import de.lariel.qualityoflife.shopkeeper.CurrencyData;
import de.lariel.qualityoflife.shopkeeper.CurrencyType;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class LarielShopkeeperConverter {

    public static List<LarielShopItem> convertToLarielShopItems(ShopkeeperDefinition def) {
        return def.levels.entrySet().stream()
                .flatMap(entry -> {
                    int level = Integer.parseInt(entry.getKey());

                    return entry.getValue().stream()
                            .filter(trade -> Math.random() <= trade.chance)
                            .map(trade -> LarielShopkeeperConverter.convertTrade(trade, level));
                })
                .toList();
    }

    private static LarielShopItem convertTrade(TradeDefinition trade, int level) {
        if (trade == null)
            throw new IllegalArgumentException("Trade definition must not be null");
        if (trade.item == null || trade.item.isBlank())
            throw new IllegalArgumentException("Trade item must not be empty");

        var item = getItem(trade.item);
        var currency = getCurrency(trade.currency);
        var buyPrice = currency.type() == CurrencyType.POKEDOLLAR ? trade.price : 0;

        return new LarielShopItem(new ShopItem(new ItemStack(item), buyPrice, 0), trade.price, currency, level);
    }

    private static @NotNull Item getItem(String itemId) {
        return Optional.ofNullable(ResourceLocation.tryParse(itemId))
                .map(BuiltInRegistries.ITEM::get)
                .filter(item -> item != Items.AIR)
                .orElseThrow(() -> new IllegalArgumentException("Unknown item: " + itemId));
    }

    private static @NotNull CurrencyData getCurrency(CurrencyJson currency) {
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
}