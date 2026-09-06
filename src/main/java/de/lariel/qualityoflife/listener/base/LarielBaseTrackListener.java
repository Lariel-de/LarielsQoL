package de.lariel.qualityoflife.listener.base;

import de.lariel.qualityoflife.network.packet.LarielTrackingHudPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.utility.LarielTrackedTarget;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class LarielBaseTrackListener<T> {

    protected final Map<UUID, LarielTrackedTarget<T>> activeTargets = new HashMap<>();

    protected void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        var data = activeTargets.get(player.getUUID());
        if (data == null) return;

        var target = data.target().get();
        if (target == null || !isTargetValid(player, target)) {
            activeTargets.remove(player.getUUID());
            LarielNetwork.sendToClient(player, new LarielTrackingHudPacket(false, 0.0F, 0));
            return;
        }

        sendCompass(player, target);
    }

    public void startTracking(ServerPlayer player, T target) {
        activeTargets.put(player.getUUID(), new LarielTrackedTarget<>(player, target));
    }

    protected abstract boolean isTargetValid(ServerPlayer player, T target);

    protected abstract double getDistance(ServerPlayer player, T target);

    protected abstract double getTargetX(T target);

    protected abstract double getTargetZ(T target);

    private float getAngleToTarget(ServerPlayer player, T target) {
        var dx = getTargetX(target) - player.getX();
        var dz = getTargetZ(target) - player.getZ();

        var angle = (float) Math.toDegrees(Math.atan2(-dx, dz));
        return (angle + 360) % 360;
    }

    private void sendCompass(ServerPlayer player, T target) {
        var angleToTarget = getAngleToTarget(player, target);
        var playerYaw = player.getYRot();
        var distance = (int) getDistance(player, target);
        var relativeAngle = (angleToTarget - playerYaw + 540) % 360 - 180;

        LarielNetwork.sendToClient(player, new LarielTrackingHudPacket(true, relativeAngle, distance));
    }
}
