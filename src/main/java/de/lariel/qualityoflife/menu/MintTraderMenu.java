package de.lariel.qualityoflife.menu;

import de.lariel.qualityoflife.menu.registry.LarielsQolModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MintTraderMenu extends AbstractContainerMenu {

    private final Container container = new SimpleContainer(9);

    public MintTraderMenu(int id, Inventory playerInv) {
        super(LarielsQolModMenus.MINT_TRADER.get(), id);

        for (var row = 0; row < 3; row++) {
            for (var col = 0; col < 3; col++) {
                this.addSlot(new Slot(container, col + row * 3,
                        30 + col * 18,   // X
                        17 + row * 18)); // Y
            }
        }

        // Player Inventory Slots
        addPlayerInventory(playerInv);
    }

    private void addPlayerInventory(Inventory inv) {
        // Hotbar
        for (var x = 0; x < 9; x++) {
            this.addSlot(new Slot(inv, x, 8 + x * 18, 142));
        }

        // Main inventory
        for (var y = 0; y < 3; y++) {
            for (var x = 0; x < 9; x++) {
                this.addSlot(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        var newStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot.hasItem()) {
            var original = slot.getItem();
            newStack = original.copy();

            var containerSlots = 9;

            if (index < containerSlots) {
                if (!this.moveItemStackTo(original, containerSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(original, 0, containerSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (original.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);

        //noinspection resource
        if (!player.level().isClientSide()) {
            for (var i = 0; i < container.getContainerSize(); i++) {
                var stack = container.getItem(i);

                if (!stack.isEmpty()) {
                    var added = player.getInventory().add(stack);

                    if (!added) {
                        player.drop(stack, false);
                    }

                    container.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public Container getContainer() {
        return container;
    }
}