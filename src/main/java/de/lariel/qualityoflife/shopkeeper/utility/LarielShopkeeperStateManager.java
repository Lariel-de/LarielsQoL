package de.lariel.qualityoflife.shopkeeper.utility;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LarielShopkeeperStateManager {
    private static final Map<ResourceLocation, LarielShopkeeperState> STATES = new HashMap<>();

    public static LarielShopkeeperState getState(ResourceLocation id) {
        return STATES.computeIfAbsent(id, k -> new LarielShopkeeperState());
    }
}