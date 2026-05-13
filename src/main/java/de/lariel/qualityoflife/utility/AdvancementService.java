package de.lariel.qualityoflife.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementService {

    public void triggerUnlockUndiscoverBreeding(ServerPlayer player) {

        var manager = player.server.getAdvancements();
        var adv = manager.get(ResourceLocation.fromNamespaceAndPath("larielsqol", "unlock_undiscovered"));

        if (adv != null) {
            player.getAdvancements().award(adv, "trigger");
        }
    }
}