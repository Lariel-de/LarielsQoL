package de.lariel.qualityoflife.entities;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.entities.client.LarielFarmerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventBusEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LarielEntityRegistration.Villager.get(), LarielFarmerRenderer::new);
    }
}
