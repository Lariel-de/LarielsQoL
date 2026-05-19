package de.lariel.qualityoflife.businessLogic;

import de.lariel.qualityoflife.utility.LarielCostEntry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LarielInventoryService {

    public static boolean playerHasAllItems(ServerPlayer player, List<LarielCostEntry> costs) {
        return costs.stream().allMatch(cost ->
                player.getInventory().countItem(cost.item()) >= cost.amount()
        );
    }

    public static void removeCostsFromPlayer(ServerPlayer player, List<LarielCostEntry> costs) {
        for (var cost : costs) {
            int remaining = cost.amount();

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (stack.is(cost.item())) {
                    int take = Math.min(stack.getCount(), remaining);
                    stack.shrink(take);
                    remaining -= take;

                    if (remaining <= 0) break;
                }
            }
        }
    }
}