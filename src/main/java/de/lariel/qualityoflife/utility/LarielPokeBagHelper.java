package de.lariel.qualityoflife.utility;

import com.pixelmonmod.pixelmon.items.PokeBagItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class LarielPokeBagHelper {
    public static void openFirstPokeBag(ServerPlayer player) {
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestStack.getItem() instanceof PokeBagItem) {
            openPokeBag(chestStack, player);
            return;
        }

        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof PokeBagItem) {
                openPokeBag(stack, player);
                return;
            }
        }

        player.sendSystemMessage(Component.translatable("key.larielsqualityoflife.no_poke_bag_found"));
    }

    private static void openPokeBag(ItemStack stack, ServerPlayer player) {
        PokeBagItem.openPokeBag(stack, player);
    }
}
