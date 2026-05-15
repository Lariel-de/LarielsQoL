package de.lariel.qualityoflife.menu.registry;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.menu.MintTraderMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LarielsQolModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, LarielsQoL.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MintTraderMenu>> MINT_TRADER =
            MENUS.register("mint_trader",
                    () -> new MenuType<>(
                            MintTraderMenu::new, // Factory
                            FeatureFlags.VANILLA_SET
                    ));
}
