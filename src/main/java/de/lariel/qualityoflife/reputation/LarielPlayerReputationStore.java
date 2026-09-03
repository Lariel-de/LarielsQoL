package de.lariel.qualityoflife.reputation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class LarielPlayerReputationStore {

    private static final String KEY = "lariel_shopkeeper_reputation";
    private final ServerPlayer player;

    public LarielPlayerReputationStore(ServerPlayer player) {
        this.player = player;
    }

    public int getLevel(ResourceLocation shopkeeperId) {
        return getData().getInt(shopkeeperId.toString());
    }

    public void addXp(ResourceLocation shopkeeperId, int xp) {
        var data = getData();
        var key = shopkeeperId.toString();
        data.putInt(key, data.getInt(key) + xp);
        saveData(data);
    }

    private CompoundTag getData() {
        var persisted = player.getPersistentData().getCompound("PlayerPersisted");
        return persisted.getCompound(KEY);
    }

    private void saveData(CompoundTag data) {
        var persisted = player.getPersistentData().getCompound("PlayerPersisted");
        persisted.put(KEY, data);
        player.getPersistentData().put("PlayerPersisted", persisted);
    }
}

