package de.lariel.qualityoflife.reputation;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LarielPlayerReputationStoreManager {

    private static final Map<UUID, LarielPlayerReputationStore> STORES = new HashMap<>();

    public static LarielPlayerReputationStore get(ServerPlayer player) {
        return STORES.computeIfAbsent(player.getUUID(), uuid -> new LarielPlayerReputationStore(player));
    }

    public static void remove(ServerPlayer player) {
        STORES.remove(player.getUUID());
    }
}