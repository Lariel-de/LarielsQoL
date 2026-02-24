package de.lariel.qualityoflife.utility;

import net.minecraft.server.level.ServerPlayer;

import java.lang.ref.WeakReference;

public record LarielTrackedTarget<T>(WeakReference<ServerPlayer> player, WeakReference<T> target) {
    public LarielTrackedTarget(ServerPlayer player, T target) {
        this(new WeakReference<>(player), new WeakReference<>(target));
    }
}
