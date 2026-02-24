package de.lariel.qualityoflife.listener;

import de.lariel.qualityoflife.utility.TrackedTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LarielEntityTrackListener {
    private static final Map<UUID, TrackedTarget> activeTargets = new HashMap<>();
    private static final String[] ARROWS = {
            "🡱", "🡲", "🡲", "🡳", "🡳", "🡰", "🡰", "🡱"
    };

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        var data = activeTargets.get(player.getUUID());
        if (data == null) return;

        var target = data.target().get();
        if (target == null || !target.isAlive()) {
            activeTargets.remove(player.getUUID());
            return;
        }

        SendCompass(player, target);
    }

    public static void StartCompassTracking(ServerPlayer player, Entity target) {
        activeTargets.put(player.getUUID(), new TrackedTarget(player, target));
    }

    private float GetAngleToTarget(ServerPlayer player, Entity target) {
        var dx = target.getX() - player.getX();
        var dz = target.getZ() - player.getZ();

        var angle = (float) Math.toDegrees(Math.atan2(-dx, dz));
        angle = (angle + 360) % 360;

        return angle;
    }

    private void SendCompass(ServerPlayer player, Entity target) {

        var angleToTarget = GetAngleToTarget(player, target);
        var playerYaw = player.getYRot();

        var compass = GetArrow(angleToTarget, playerYaw);

        double distance = player.distanceTo(target);

        Component msg = Component.literal(compass)
                .append("  §b")
                .append(target.getDisplayName())
                .append(" ")
                .append((int) distance + "m");

        SendActionBar(player, msg);
    }

    private void SendActionBar(ServerPlayer player, Component component) {
        player.connection.send(new ClientboundSetActionBarTextPacket(component));
    }

    private String GetArrow(float angleToTarget, float playerYaw) {
        var relative = (angleToTarget - playerYaw + 360) % 360;
        var index = Math.round(relative / 45f) % 8;

        return ARROWS[index];
    }
}