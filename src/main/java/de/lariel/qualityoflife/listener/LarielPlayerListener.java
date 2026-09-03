package de.lariel.qualityoflife.listener;

import de.lariel.qualityoflife.reputation.LarielPlayerReputationStoreManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class LarielPlayerListener {

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        var original = event.getOriginal();
        var clone = event.getEntity();

        var originalPersisted = original.getPersistentData().getCompound("PlayerPersisted");
        clone.getPersistentData().put("PlayerPersisted", originalPersisted.copy());
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            LarielPlayerReputationStoreManager.remove(player);
        }
    }
}
