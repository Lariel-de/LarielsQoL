package de.lariel.qualityoflife.client.screen.registry;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.client.screen.LarielMintTraderScreen;
import de.lariel.qualityoflife.menu.registry.LarielsQolModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, value = Dist.CLIENT)
public class LarielsQoLModScreens {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(LarielsQolModMenus.MINT_TRADER.get(), LarielMintTraderScreen::new);
    }
}

