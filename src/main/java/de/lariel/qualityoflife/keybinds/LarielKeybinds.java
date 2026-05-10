package de.lariel.qualityoflife.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import de.lariel.qualityoflife.LarielsQoL;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, value = Dist.CLIENT)
public class LarielKeybinds {
    public static final Lazy<KeyMapping> OPEN_POKE_BAG_HOTKEY = Lazy.of(() -> new KeyMapping(
            "key.larielsqualityoflife.open_poke_bag",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.larielsqualityoflife.category_name"
    ));

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_POKE_BAG_HOTKEY.get());
    }
}
