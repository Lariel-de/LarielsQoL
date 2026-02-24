package de.lariel.qualityoflife.listener;

import de.lariel.qualityoflife.listener.base.LarielBaseTrackListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class LarielEntityTrackListener extends LarielBaseTrackListener<Entity> {
    private static LarielEntityTrackListener _instance;

    public static LarielEntityTrackListener GetInstance() {
        if (_instance == null)
            _instance = new LarielEntityTrackListener();

        return _instance;
    }
    @Override
    protected boolean isTargetValid(Entity target) {
        return target.isAlive();
    }

    @Override
    protected double getDistance(ServerPlayer player, Entity target) {
        return player.distanceTo(target);
    }

    @Override
    protected double getTargetX(Entity target) {
        return target.getX();
    }

    @Override
    protected double getTargetZ(Entity target) {
        return target.getZ();
    }
}