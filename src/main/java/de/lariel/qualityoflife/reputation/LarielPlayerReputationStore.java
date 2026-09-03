package de.lariel.qualityoflife.reputation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class LarielPlayerReputationStore {

    private static final String KEY = "lariel_shopkeeper_reputation";
    private final Player player;
    private final double scaleFactor = 100.0;
    private final int maxLevel;

    public LarielPlayerReputationStore(Player player) {
        this.player = player;
        this.maxLevel = 5;
    }

    public int getTotalXp(ResourceLocation shopkeeperId) {
        return getData().getInt(shopkeeperId.toString());
    }

    public int getLevel(ResourceLocation shopkeeperId) {
        int totalXp = getTotalXp(shopkeeperId);
        int level = (int) Math.floor(Math.sqrt(totalXp / scaleFactor));
        return Math.min(5, level);
    }

    public int getXpInCurrentLevel(ResourceLocation shopkeeperId) {
        int totalXp = getTotalXp(shopkeeperId);
        int level = getLevel(shopkeeperId);

        int levelBaseXp = (int) (scaleFactor * Math.pow(level, 2));
        return totalXp - levelBaseXp;
    }

    public int getNextLevelXp(ResourceLocation shopkeeperId) {
        int level = getLevel(shopkeeperId);

        if (level >= 5) return 0;

        int currentLevelBaseXp = getLevelTotalXp(level);
        int nextLevelBaseXp = getLevelTotalXp(level + 1);

        return nextLevelBaseXp - currentLevelBaseXp;
    }

    private int getLevelTotalXp(int level) {
        return (int) (scaleFactor * Math.pow(level, 2));
    }

    public void addXp(ResourceLocation shopkeeperId, int xp) {
        int totalXp = getTotalXp(shopkeeperId);

        if (totalXp >= getLevelTotalXp(maxLevel)) return;

        int newXp = Math.min(getLevelTotalXp(maxLevel), totalXp + xp);

        var data = getData();
        data.putInt(shopkeeperId.toString(), newXp);
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