package de.lariel.qualityoflife.entities;

import de.lariel.qualityoflife.entities.custom.LarielVillager;
import de.lariel.qualityoflife.LarielsQoL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LarielEntityRegistration {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LarielsQoL.MOD_ID);

    public static final Supplier<EntityType<LarielVillager>> Villager =
            ENTITY_TYPES.register("lariel_villager", () -> EntityType.Builder.of(LarielVillager::new, MobCategory.MISC)
                    .sized(0.6F, 1.95F)
                    .build("lariel_villager"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}