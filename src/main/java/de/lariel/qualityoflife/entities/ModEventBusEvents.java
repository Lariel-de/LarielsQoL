package de.lariel.qualityoflife.entities;

import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.entities.custom.LarielVillager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(LarielEntityRegistration.Villager.get(), LarielVillager.createAttributes().build());
    }
}
