package de.lariel.qualityoflife.listener;

import de.lariel.qualityoflife.listener.base.LarielBaseTrackListener;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class LarielBlockTrackListener extends LarielBaseTrackListener<BlockPos> {
    private static LarielBlockTrackListener _instance;

    public static LarielBlockTrackListener GetInstance() {
        if (_instance == null)
            _instance = new LarielBlockTrackListener();

        return _instance;
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        super.onPlayerTick(event);
    }

    @Override
    protected boolean isTargetValid(BlockPos target) {
        return true; // BlockPos stirbt nicht
    }

    @Override
    protected double getDistance(ServerPlayer player, BlockPos target) {
        return Math.sqrt(player.distanceToSqr(target.getX(), target.getY(), target.getZ()));
    }

    @Override
    protected double getTargetX(BlockPos target) {
        return target.getX() + 0.5; // Mitte des Blocks
    }

    @Override
    protected double getTargetZ(BlockPos target) {
        return target.getZ() + 0.5;
    }
}