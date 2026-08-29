package de.lariel.qualityoflife.items;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.entities.LarielEntityRegistration;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LarielItemRegistration {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LarielsQoL.MOD_ID);

    public static final DeferredItem<SpawnEggItem> LARIEL_VILLAGER_SPAWN_EGG = ITEMS.register("lariel_villager_spawn_egg",
            () -> new DeferredSpawnEggItem(LarielEntityRegistration.Villager, 0x31afaf, 0xffac00,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}