package de.lariel.qualityoflife.utility;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.lang.ref.WeakReference;

public record TrackedTarget(WeakReference<ServerPlayer> player, WeakReference<Entity> target) {
    public TrackedTarget(ServerPlayer player, Entity target) {
        this(new WeakReference<>(player), new WeakReference<>(target));
    }
}
