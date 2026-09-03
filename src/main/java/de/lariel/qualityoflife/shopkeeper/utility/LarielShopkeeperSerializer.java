package de.lariel.qualityoflife.shopkeeper.utility;

import com.google.gson.reflect.TypeToken;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.shopkeeper.data.LarielShopItemJson;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;

import java.util.List;

public class LarielShopkeeperSerializer {

    public static String serialize(List<LarielShopItem> items, HolderLookup.Provider registries) {
        var dtoList = items.stream()
                .map(item -> toDto(item, registries))
                .toList();

        return LarielsQoL.GSON.toJson(dtoList);
    }

    public static List<LarielShopItem> deserialize(String json, HolderLookup.Provider registries) {
        List<LarielShopItemJson> dtoList =
                LarielsQoL.GSON.fromJson(json, new TypeToken<List<LarielShopItemJson>>() {
                }.getType());

        return dtoList.stream()
                .map(dto -> fromDto(dto, registries))
                .toList();
    }

    private static LarielShopItemJson toDto(LarielShopItem item, HolderLookup.Provider registries) {
        var itemNbt = LarielItemStackFactory.serialize(item.getShopItem().itemStack(), registries);
        var currencyNbt = item.getCurrencyData().currencyItem() == null
                ? null
                : LarielItemStackFactory.serialize(item.getCurrencyData().currencyItem(), registries);

        return new LarielShopItemJson(
                item.getShopItem().uuid().toString(),
                BuiltInRegistries.ITEM.getKey(item.getShopItem().itemStack().getItem()).toString(),
                itemNbt,
                item.getPrice(),
                item.getCurrencyData().type().name(),
                item.getCurrencyData().currencyItem() != null
                        ? BuiltInRegistries.ITEM.getKey(item.getCurrencyData().currencyItem().getItem()).toString()
                        : null,
                currencyNbt,
                item.getCurrencyData().customKey(),
                item.getMinLevel(),
                item.getXp(),
                item.getMaxSellCountPerDay()
        );
    }

    private static LarielShopItem fromDto(LarielShopItemJson dto, HolderLookup.Provider registries) {
        return dto.toLarielShopItem(registries);
    }
}