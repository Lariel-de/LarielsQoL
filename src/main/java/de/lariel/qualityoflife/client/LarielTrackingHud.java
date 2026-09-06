package de.lariel.qualityoflife.client;

import de.lariel.qualityoflife.LarielsQoL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = LarielsQoL.MOD_ID, value = Dist.CLIENT)
public final class LarielTrackingHud {
    private static final int ARROW_SHADOW = 0xB0000000;
    private static final int ARROW_DARK = 0xFF176A85;
    private static final int ARROW_LIGHT = 0xFF55D8F2;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private static boolean active;
    private static float targetAngle;
    private static float renderedAngle;
    private static float verticalAngle;
    private static int distance;

    private LarielTrackingHud() {
    }

    public static void update(boolean tracking, float relativeAngle, float targetVerticalAngle, int targetDistance) {
        active = tracking;
        if (!tracking) {
            return;
        }

        targetAngle = relativeAngle;
        verticalAngle = targetVerticalAngle;
        distance = Math.max(0, targetDistance);
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        update(false, 0.0F, 0.0F, 0);
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!active || minecraft.player == null || minecraft.screen != null) {
            return;
        }

        renderedAngle += wrapDegrees(targetAngle - renderedAngle) * 0.35F;

        GuiGraphics graphics = event.getGuiGraphics();
        int centerX = graphics.guiWidth() / 2;
        int centerY = 36;

        graphics.pose().pushPose();
        graphics.pose().translate(centerX, centerY, 0);
        graphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(renderedAngle));

        drawArrow(graphics, 2, 2, ARROW_SHADOW);
        drawArrow(graphics, 0, 0, ARROW_DARK);
        drawArrow(graphics, -1, -1, ARROW_LIGHT);

        graphics.pose().popPose();

        drawVerticalIndicator(graphics, centerX + 22, centerY);

        String distanceText = distance + "m";
        graphics.drawString(
                minecraft.font,
                distanceText,
                centerX - minecraft.font.width(distanceText) / 2,
                centerY + 19,
                TEXT_COLOR,
                true
        );
    }

    private static void drawArrow(GuiGraphics graphics, int offsetX, int offsetY, int color) {
        graphics.fill(offsetX - 2, offsetY - 4, offsetX + 3, offsetY + 12, color);
        graphics.fill(offsetX - 8, offsetY - 4, offsetX + 9, offsetY + 1, color);
        graphics.fill(offsetX - 6, offsetY - 8, offsetX + 7, offsetY - 4, color);
        graphics.fill(offsetX - 4, offsetY - 11, offsetX + 5, offsetY - 8, color);
    }

    private static void drawVerticalIndicator(GuiGraphics graphics, int x, int y) {
        if (Math.abs(verticalAngle) < 5.0F) {
            return;
        }

        boolean targetIsAbove = verticalAngle > 0.0F;
        int color = targetIsAbove ? ARROW_LIGHT : ARROW_DARK;
        int length = 6 + Math.min(7, (int) (Math.abs(verticalAngle) / 10.0F));

        graphics.fill(x - 1, y - length, x + 2, y + length + 1, color);
        if (targetIsAbove) {
            graphics.fill(x - 5, y - length, x + 6, y - length + 3, color);
            graphics.fill(x - 3, y - length - 3, x + 4, y - length, color);
        } else {
            graphics.fill(x - 5, y + length - 2, x + 6, y + length + 1, color);
            graphics.fill(x - 3, y + length + 1, x + 4, y + length + 4, color);
        }
    }

    private static float wrapDegrees(float angle) {
        return (angle + 540.0F) % 360.0F - 180.0F;
    }
}
