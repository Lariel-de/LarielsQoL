package de.lariel.qualityoflife.reputation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LarielPlayerReputationStore {
    private final Map<ResourceLocation, Integer> reputation = new HashMap<>();

    private LarielPlayerReputationStore() {
    }

    public int getLevel(ResourceLocation shopkeeperId) {
        return reputation.getOrDefault(shopkeeperId, 0);
    }

    public void addXp(ResourceLocation shopkeeperId, int xp) {
        int current = reputation.getOrDefault(shopkeeperId, 0);
        reputation.put(shopkeeperId, current + xp);
    }

    public static LarielPlayerReputationStore get(@NotNull ServerPlayer player) {
        return new LarielPlayerReputationStore();
    }
}