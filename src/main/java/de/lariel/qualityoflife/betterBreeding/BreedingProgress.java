package de.lariel.qualityoflife.betterBreeding;

import net.minecraft.server.level.ServerPlayer;

public class BreedingProgress {

    private static final String KEY_COUNT = "betterbreeding_count";
    private static final String KEY_BABY = "betterbreeding_baby";

    public static int getCount(ServerPlayer player) {
        return player.getPersistentData().getInt(KEY_COUNT);
    }

    public static void incrementCount(ServerPlayer player) {
        int current = getCount(player);
        player.getPersistentData().putInt(KEY_COUNT, current + 1);
    }

    public static boolean hasBredBaby(ServerPlayer player) {

        return player.getPersistentData().getBoolean(KEY_BABY);
    }

    public static void setBredBaby(ServerPlayer player) {

        player.getPersistentData().putBoolean(KEY_BABY, true);
    }

    public static void reset(ServerPlayer player) {
        var data = player.getPersistentData();
        data.remove(KEY_COUNT);
        data.remove(KEY_BABY);
    }
}
