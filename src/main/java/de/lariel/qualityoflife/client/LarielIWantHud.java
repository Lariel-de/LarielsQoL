package de.lariel.qualityoflife.client;

import com.pixelmonmod.pixelmon.client.gui.battles.BattleScreen;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.network.packet.LarielIWantPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, value = Dist.CLIENT)
public final class LarielIWantHud {
    private static final int BUTTON_WIDTH = 58;
    private static final int BUTTON_HEIGHT = 12;
    private static final Set<UUID> WILD_TARGETS = new HashSet<>();
    private static UUID protectedPokemon;

    private LarielIWantHud() {
    }

    public static void update(List<UUID> wildTargets, UUID selectedPokemon) {
        WILD_TARGETS.clear();
        WILD_TARGETS.addAll(wildTargets);
        protectedPokemon = selectedPokemon;
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        var minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof BattleScreen screen)
                || screen.bm == null
                || screen.bm.displayedEnemyPokemon == null
                || WILD_TARGETS.isEmpty()) {
            return;
        }

        var enemies = screen.bm.displayedEnemyPokemon;
        var scale = Math.min(1.0F, screen.width / (180.0F * Math.max(1, enemies.length)));
        var graphics = event.getGuiGraphics();

        for (var index = 0; index < enemies.length; index++) {
            var enemy = enemies[index];
            if (enemy == null || !WILD_TARGETS.contains(enemy.pokemonUUID)) {
                continue;
            }

            var x = Math.round((180 * index + 44) * scale);
            var y = Math.round((enemy.xPos + 32) * scale);
            drawButton(graphics, minecraft, x, y, scale, protectedPokemon != null && protectedPokemon.equals(enemy.pokemonUUID));
        }
    }

    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getAction() != GLFW.GLFW_PRESS) {
            return;
        }

        var minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof BattleScreen screen)
                || screen.bm == null
                || screen.bm.displayedEnemyPokemon == null
                || WILD_TARGETS.isEmpty()) {
            return;
        }

        var mouseX = minecraft.mouseHandler.xpos() * screen.width / minecraft.getWindow().getWidth();
        var mouseY = minecraft.mouseHandler.ypos() * screen.height / minecraft.getWindow().getHeight();
        var enemies = screen.bm.displayedEnemyPokemon;
        var scale = Math.min(1.0F, screen.width / (180.0F * Math.max(1, enemies.length)));

        for (var index = 0; index < enemies.length; index++) {
            var enemy = enemies[index];
            if (enemy == null || !WILD_TARGETS.contains(enemy.pokemonUUID)) {
                continue;
            }

            var x = Math.round((180 * index + 44) * scale);
            var y = Math.round((enemy.xPos + 32) * scale);
            if (mouseX >= x && mouseX < x + BUTTON_WIDTH * scale
                    && mouseY >= y && mouseY < y + BUTTON_HEIGHT * scale) {
                LarielNetwork.sendToServer(new LarielIWantPacket(enemy.pokemonUUID));
                event.setCanceled(true);
                return;
            }
        }
    }

    private static void drawButton(GuiGraphics graphics, Minecraft minecraft, int x, int y, float scale, boolean active) {
        var width = Math.round(BUTTON_WIDTH * scale);
        var height = Math.round(BUTTON_HEIGHT * scale);
        var background = active ? 0xFF2E8B57 : 0xFF3F3F3F;

        graphics.fill(x, y, x + width, y + height, 0xFF111111);
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, background);
        var text = net.minecraft.network.chat.Component.translatable("capture.larielsqualityoflife.i_want");
        graphics.drawString(minecraft.font, text, x + (width - minecraft.font.width(text)) / 2, y + 2, 0xFFFFFFFF, false);

        if (active) {
            graphics.fill(x + width + 3, y + 3, x + width + 7, y + 7, 0xFF55FF55);
        }
    }
}
