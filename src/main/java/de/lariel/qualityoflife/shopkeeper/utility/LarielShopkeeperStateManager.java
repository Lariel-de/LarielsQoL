package de.lariel.qualityoflife.shopkeeper.utility;

import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LarielShopkeeperStateManager {
    private static final Map<ResourceLocation, LarielShopkeeperState> STATES = new HashMap<>();

    public static LarielShopkeeperState getState(ResourceLocation id) {
        return STATES.computeIfAbsent(id, k -> new LarielShopkeeperState());
    }

    public static Optional<LarielShopItem> findItem(ResourceLocation shopkeeperId, UUID itemId) {
        var state = STATES.get(shopkeeperId);
        if (state == null) return Optional.empty();

        return state.findItem(itemId);
    }
}