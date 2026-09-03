package de.lariel.qualityoflife.listener;

import com.google.gson.JsonElement;
import de.lariel.qualityoflife.data.ShopkeeperDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static de.lariel.qualityoflife.LarielsQoL.GSON;

public class ShopkeeperReloadListener extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceLocation, ShopkeeperDefinition> SHOPKEEPERS = new HashMap<>();

    public ShopkeeperReloadListener() {
        super(GSON, "shopoffers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {

        SHOPKEEPERS.clear();

        for (var entry : elements.entrySet()) {
            var id = entry.getKey();
            var json = entry.getValue();

            var def = GSON.fromJson(json, ShopkeeperDefinition.class);
            SHOPKEEPERS.put(id, def);
        }
    }
}