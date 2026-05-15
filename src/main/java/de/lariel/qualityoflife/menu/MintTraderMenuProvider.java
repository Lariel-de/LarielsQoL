package de.lariel.qualityoflife.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MintTraderMenuProvider implements MenuProvider {

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Mint Trader");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new MintTraderMenu(i, inventory);
    }
}

