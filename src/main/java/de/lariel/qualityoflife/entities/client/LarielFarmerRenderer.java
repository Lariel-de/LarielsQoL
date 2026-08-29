package de.lariel.qualityoflife.entities.client;

import de.lariel.qualityoflife.entities.custom.LarielVillager;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LarielFarmerRenderer extends MobRenderer<LarielVillager, VillagerModel<LarielVillager>> {

    public LarielFarmerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VillagerModel<>(ctx.bakeLayer(ModelLayers.VILLAGER)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LarielVillager entity) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/villager/villager.png");
    }
}

