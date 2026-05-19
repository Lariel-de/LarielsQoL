package de.lariel.qualityoflife.businessLogic.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import de.lariel.qualityoflife.businessLogic.LarielInventoryService;
import de.lariel.qualityoflife.network.packet.LarielBetterBreedingStatusPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.utility.LarielCostEntry;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LarielBreedingApplyService {

    public static void apply(ServerPlayer player, UUID eggUUID, String form, String palette, String gender) {

        var storage = StorageProxy.getPartyNow(player.getUUID());
        if (storage == null) return;

        Pokemon egg = storage.getTeam(p -> p.getUUID().equals(eggUUID)).getFirst();
        if (egg == null) return;

        List<LarielCostEntry> costs = new ArrayList<>();
        LarielCostService.addFormCostIfChanged(costs, egg, form);
        LarielCostService.addPaletteCostIfChanged(costs, egg, palette);
        LarielCostService.addGenderCostIfChanged(costs, egg, gender);

        if (!LarielInventoryService.playerHasAllItems(player, costs)) {
            LarielNetwork.sendToClient(player,
                    new LarielBetterBreedingStatusPacket("betterbreeding.larielsqualityoflife.notenoughitems", false));
            return;
        }

        LarielInventoryService.removeCostsFromPlayer(player, costs);

        egg.setForm(form);
        egg.setPalette(palette);
        egg.setGender(Gender.valueOf(gender));

        storage.set(egg.getPosition(), egg);

        LarielNetwork.sendToClient(player,
                new LarielBetterBreedingStatusPacket("betterbreeding.larielsqualityoflife.applied", true));
    }
}
