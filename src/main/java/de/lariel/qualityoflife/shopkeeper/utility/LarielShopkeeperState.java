package de.lariel.qualityoflife.shopkeeper.utility;

import de.lariel.qualityoflife.shopkeeper.data.ShopkeeperDefinition;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LarielShopkeeperState {
    private long lastDay = -1;
    private List<LarielShopItem> todaysItems = List.of();

    public List<LarielShopItem> getItemsForToday(ServerLevel level, ShopkeeperDefinition def, int shopkeeperLevel) {
        long day = level.getDayTime() / 24000L;

        if (day != lastDay) {
            lastDay = day;

            todaysItems = LarielShopkeeperConverter.convertToLarielShopItems(def);
        }

        return todaysItems.stream()
                .filter(item -> item.getMinLevel() <= shopkeeperLevel)
                .toList();
    }

    public Optional<LarielShopItem> findItem(UUID id) {
        return todaysItems.stream()
                .filter(item -> item.getShopItem().uuid().equals(id))
                .findFirst();
    }
}