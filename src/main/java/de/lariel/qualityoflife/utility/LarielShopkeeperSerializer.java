package de.lariel.qualityoflife.utility;

import com.google.gson.reflect.TypeToken;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.data.LarielShopItemJson;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public class LarielShopkeeperSerializer {

    public static String serialize(List<LarielShopItem> items) {
        var dtoList = items.stream()
                .map(LarielShopkeeperSerializer::toDto)
                .toList();

        return LarielsQoL.GSON.toJson(dtoList);
    }

    public static List<LarielShopItem> deserialize(String json) {
        List<LarielShopItemJson> dtoList =
                LarielsQoL.GSON.fromJson(json, new TypeToken<List<LarielShopItemJson>>() {
                }.getType());

        return dtoList.stream()
                .map(LarielShopkeeperSerializer::fromDto)
                .toList();
    }

    private static LarielShopItemJson toDto(LarielShopItem item) {
        return new LarielShopItemJson(
                BuiltInRegistries.ITEM.getKey(item.getShopItem().itemStack().getItem()).toString(),
                item.getPrice(),
                item.getCurrencyData().type().name(),
                item.getCurrencyData().currencyItem() != null
                        ? BuiltInRegistries.ITEM.getKey(item.getCurrencyData().currencyItem().getItem()).toString()
                        : null,
                item.getCurrencyData().customKey(),
                0
        );
    }

    private static LarielShopItem fromDto(LarielShopItemJson dto) {
        return dto.toLarielShopItem();
    }
}