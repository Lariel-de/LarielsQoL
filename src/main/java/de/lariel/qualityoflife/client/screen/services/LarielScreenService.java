package de.lariel.qualityoflife.client.screen.services;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class LarielScreenService {
    public static void openScreen(Screen screen) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(screen));
    }
}
