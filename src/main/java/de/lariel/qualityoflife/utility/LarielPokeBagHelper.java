package de.lariel.qualityoflife.utility;

import com.pixelmonmod.pixelmon.items.PokeBagItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class LarielPokeBagHelper {
    public static void openFirstPokeBag(ServerPlayer player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof PokeBagItem) {
                PokeBagItem.openPokeBag(stack, player);
                return;
            }
        }

        player.sendSystemMessage(Component.translatable("key.larielsqualityoflife.no_poke_bag_found"));
    }
}

