package de.lariel.qualityoflife.client.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class LarielDropdownWidget extends AbstractWidget {

    private final List<String> options;
    private final Consumer<String> onSelect;
    private boolean expanded = false;
    private String selected;

    public LarielDropdownWidget(int x, int y, int width, int height,
                                List<String> options, String defaultValue,
                                Consumer<String> onSelect) {
        super(x, y, width, height, Component.literal(defaultValue));
        this.options = options;
        this.selected = defaultValue;
        this.onSelect = onSelect;
    }

    @Override
    protected void renderWidget(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        // Hintergrund
        gfx.fill(getX(), getY(), getX() + width, getY() + height, 0xFF333333);

        // Text
        gfx.drawString(Minecraft.getInstance().font, selected, getX() + 4, getY() + 4, 0xFFFFFF);

        // Wenn expanded → Optionen anzeigen
        if (!expanded) {
            return;
        }

        gfx.pose().pushPose();
        gfx.pose().translate(0, 0, 1); // ganz nach vorne

        int yOff = height;
        for (String opt : options) {
            gfx.fill(getX(), getY() + yOff, getX() + width, getY() + yOff + height, 0xFF222222);
            gfx.drawString(Minecraft.getInstance().font, opt, getX() + 4, getY() + yOff + 4, 0xFFFFFF);
            yOff += height;
        }

        gfx.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY) && !isClickInsideOptions(mouseX, mouseY)) {
            expanded = false;
            return false;
        }

        // Klick auf Hauptfeld → expand toggeln
        if (isMouseOver(mouseX, mouseY)) {
            expanded = !expanded;
            return true;
        }

        // Auswahl treffen
        int index = (int) ((mouseY - getY()) / height) - 1;
        if (index >= 0 && index < options.size()) {
            selected = options.get(index);
            setMessage(Component.literal(selected));
            onSelect.accept(selected);
        }

        expanded = false;
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public String getSelected() {
        return selected;
    }

    private boolean isClickInsideOptions(double mouseX, double mouseY) {
        if (!expanded) return false;

        int yStart = getY() + height;
        int yEnd = yStart + height * options.size();

        return mouseX >= getX() && mouseX <= getX() + width &&
                mouseY >= yStart && mouseY <= yEnd;
    }
}
