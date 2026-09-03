package de.lariel.qualityoflife.items;

import de.lariel.qualityoflife.LarielsQoL;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LarielCreateModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LarielsQoL.MOD_ID);

    @SuppressWarnings("unused")
    public static final Supplier<CreativeModeTab> LARIELS_AUTOMATIC_FARMS = CREATIVE_MODE_TAB.register("lariels_automatic_farms",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(LarielItemRegistration.LARIEL_VILLAGER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.larielsqualityoflife.tabtitle"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(LarielItemRegistration.LARIEL_VILLAGER_SPAWN_EGG);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
