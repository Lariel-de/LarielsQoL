package de.lariel.qualityoflife.client.screen;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.gui.npc.widget.DropDownWidget;
import com.pixelmonmod.pixelmon.client.gui.npc.widget.ScrollableListWidget;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielCostService;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import de.lariel.qualityoflife.network.packet.LarielBetterBreedingApplyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LarielBetterBreedingScreen extends Screen {// Layout
    private static final int PANEL_HEIGHT = 180;
    private static final int LEFT_PANEL_WIDTH = 120;
    private static final int RIGHT_PANEL_WIDTH = 200;
    private static final int PANEL_SPACING = 20;

    // Egg list
    private final List<Pokemon> eggs = new ArrayList<>();
    private final LarielsQolBetterBreedingConfig config;
    private int selectedEggIndex = -1;

    // Status
    private Component statusMessage = null;
    private long statusUntil = 0;
    private int statusColor = 0xFFFFFFFF;

    private String selectedForm;
    private String selectedPalette;
    private String selectedGender;
    private ScrollableListWidget editorList;
    private Pokemon preview;
    private Pokemon egg;

    public LarielBetterBreedingScreen() {
        super(Component.literal("Better Breeding"));

        this.config = LarielsQoL.getConfig().breeding();
    }

    @Override
    protected void init() {
        super.init();
        loadEggs();
    }

    private void loadEggs() {
        eggs.clear();
        Player player = Minecraft.getInstance().player;

        if (player == null) return;

        // Pixelmon API: Party Pokémon
        var storage = StorageProxy.getPartyNow(player.getUUID());
        if (storage == null) return;

        var party = storage.getTeam(PokemonBase::isEgg);
        if (party == null) return;

        eggs.addAll(party);
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gfx, mouseX, mouseY, partialTicks);

        // Titel
        gfx.drawString(this.font, "Better Breeding", this.width / 2 - 50, 20, 0xFFFFFF);

        // Layout berechnen
        int totalWidth = LEFT_PANEL_WIDTH + PANEL_SPACING + RIGHT_PANEL_WIDTH;
        int startX = (this.width - totalWidth) / 2;

        int leftX = startX;
        int rightX = startX + LEFT_PANEL_WIDTH + PANEL_SPACING;
        int y = 50;

        renderLeftPanel(gfx, leftX, y);
        renderRightPanel(gfx, rightX, y, partialTicks);

        super.render(gfx, mouseX, mouseY, partialTicks);

        renderStatusMessage(gfx);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xAA000000);
    }

    private void renderLeftPanel(GuiGraphics gfx, int x, int y) {
        gfx.fill(x, y, x + LEFT_PANEL_WIDTH, y + PANEL_HEIGHT, 0xAA000000);
        gfx.drawString(this.font, "Eggs", x + 6, y + 6, 0xFFFFFF);

        int entryY = y + 20;

        for (int i = 0; i < eggs.size(); i++) {
            Pokemon egg = eggs.get(i);

            int color = (i == selectedEggIndex) ? 0xFF55FF55 : 0xFFFFFFFF;

            ScreenHelper.drawImageQuad(
                    egg.getSprite(),
                    gfx,
                    x + 4,
                    entryY - 6,
                    16, 16,
                    0, 0, 1, 1,
                    1
            );

            gfx.drawString(
                    this.font,
                    egg.getSpecies().getName(),
                    x + 24,
                    entryY,
                    color
            );

            entryY += 12;
        }
    }

    private void renderRightPanel(GuiGraphics gfx, int x, int y, float partialTicks) {
        gfx.fill(x, y, x + RIGHT_PANEL_WIDTH, y + PANEL_HEIGHT, 0xAA000000);
        gfx.drawString(this.font, "Editor", x + 6, y + 6, 0xFFFFFF);

        if (selectedEggIndex == -1) {
            gfx.drawString(this.font, "Select an egg", x + 6, y + 30, 0xAAAAAA);
            return;
        }

        gfx.drawString(this.font, "Species: " + egg.getSpecies().getName(), x + 6, y + 30, 0xFFFFFF);

        int spriteX = x + RIGHT_PANEL_WIDTH - 40; // rechtsbündig
        int spriteY = y + 10;

        if (selectedForm != null)
            preview.setForm(selectedForm);

        if (selectedPalette != null)
            preview.setPalette(selectedPalette);

        if (selectedGender != null)
            preview.setGender(Gender.valueOf(selectedGender));

        // Sprite rendern
        ScreenHelper.drawImageQuad(
                preview.getSprite(),
                gfx,
                spriteX,
                spriteY,
                32, 32,   // Größe
                0, 0, 1, 1,
                1
        );

        int costX = spriteX - 50;
        int costY = y + 65;

        var formCosts = LarielCostService.CalculateFormCosts(egg, selectedForm);
        if (formCosts != null)
            renderCost(gfx, costX, costY,
                    formCosts.item(),
                    formCosts.amount()
            );

        var paletteCosts = LarielCostService.CalculatePaletteCosts(egg, selectedPalette);
        if (paletteCosts != null)
            renderCost(gfx, costX, costY + 30,
                    paletteCosts.item(),
                    paletteCosts.amount()
            );

        var genderCosts = LarielCostService.CalculateGenderCosts(egg, selectedGender);
        if (genderCosts != null)
            renderCost(gfx, costX, costY + 60,
                    genderCosts.item(),
                    genderCosts.amount()
            );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int totalWidth = LEFT_PANEL_WIDTH + PANEL_SPACING + RIGHT_PANEL_WIDTH;
        int startX = (this.width - totalWidth) / 2;
        int leftX = startX;
        int y = 50;

        // Klick in Egg-Liste?
        if (mouseX >= leftX && mouseX <= leftX + LEFT_PANEL_WIDTH &&
                mouseY >= y + 20 && mouseY <= y + PANEL_HEIGHT) {

            int index = (int) ((mouseY - (y + 20)) / 12);

            if (index >= 0 && index < eggs.size()) {
                selectedEggIndex = index;
                setupDropdowns();
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderStatusMessage(GuiGraphics gfx) {
        if (statusMessage != null && System.currentTimeMillis() < statusUntil) {
            int x = this.width / 2 - this.font.width(statusMessage) / 2;
            int y = this.height - 30;
            gfx.drawString(this.font, statusMessage, x, y, statusColor);
        }
    }

    private void renderCost(GuiGraphics gfx, int x, int y, Item item, int amount) {
        if (amount <= 0) return;

        // Icon rendern
        gfx.renderItem(new ItemStack(item), x, y);

        // Text daneben
        gfx.drawString(this.font, "x" + amount, x + 18, y + 5, 0xFFFFFF);
    }

    public void showStatus(Component msg, int durationTicks, boolean success) {
        this.statusMessage = msg;
        this.statusColor = success ? 0xFF55FF55 : 0xFFFF5555;
        this.statusUntil = System.currentTimeMillis() + durationTicks * 50L;
    }

    private void setupDropdowns() {
        if (selectedEggIndex == -1) return;

        egg = eggs.get(selectedEggIndex);
        preview = PokemonBuilder.copy(egg).egg(false).build();
        preview.hatchEgg();

        if (editorList != null) {
            removeWidget(editorList);
        }

        editorList = initEditorList();

        createFormDropDown(editorList, egg);
        createPaletteDropDown(editorList, egg);
        createGenderDropDown(editorList, egg);

        var applyButton = Button.builder(Component.translatable("Apply"),
                        btn -> sendApplyPacket())
                .size(80, 20)
                .build();

        editorList.addWidgets(applyButton);

        addRenderableWidget(editorList);
    }

    private @NotNull ScrollableListWidget<? extends LayoutElement> initEditorList() {
        return new ScrollableListWidget<>(
                this.width / 2 - 28, // x
                100,                  // y
                RIGHT_PANEL_WIDTH,   // width
                PANEL_HEIGHT        // height
        );
    }

    private void createFormDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        List<Stats> forms = egg.getSpecies().getForms(true);
        DropDownWidget<Stats> formDropDown = new DropDownWidget<>(90, 20);
        formDropDown.setOptionConverter(p -> Component.translatable(p.getTranslationKey()).getString());
        formDropDown.setOptions(Lists.newArrayList(forms), egg.getForm());
        formDropDown.setOrdered();
        formDropDown.setOnSelected(p -> selectedForm = p.getName());
        selectedForm = formDropDown.getSelected().getName();
        scrollableListWidget.addWidgets(new StringWidget(Component.translatable("gui.pokemoneditor.form"), this.font), formDropDown);
    }

    private void createPaletteDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        List<PaletteProperties> palettes = Lists.newArrayList(egg.getForm().getGenderProperties(egg.getGender()).getPalettes());
        palettes.removeIf(p -> p.getName().toLowerCase().contains("shiny") && !config.getShinyGuaranteeEnabled());
        DropDownWidget<PaletteProperties> paletteDropDownList = new DropDownWidget<>(90, 20);
        paletteDropDownList.setOptionConverter(p -> Component.translatable(p.getTranslationKey()).getString());
        paletteDropDownList.setOptions(Lists.newArrayList(palettes), egg.getPalette());
        paletteDropDownList.setOrdered();
        paletteDropDownList.setOnSelected(p -> selectedPalette = p.getName());
        selectedPalette = paletteDropDownList.getSelected().getName();
        scrollableListWidget.addWidgets(new StringWidget(Component.translatable("gui.pokemoneditor.palette"), this.font), paletteDropDownList);
    }

    private void createGenderDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        List<Gender> genders = Lists.newArrayList(egg.getForm().getPossibleGenders());
        DropDownWidget<Gender> genderDropDownList = new DropDownWidget<>(90, 20);
        genderDropDownList.setOptionConverter(p -> Component.translatable(p.getTranslationKey()).getString());
        genderDropDownList.setOptions(Lists.newArrayList(genders), egg.getGender());
        genderDropDownList.setOrdered();
        genderDropDownList.setOnSelected(p -> selectedGender = p.name());
        selectedGender = genderDropDownList.getSelected().name();
        scrollableListWidget.addWidgets(new StringWidget(Component.translatable("gui.pokemoneditor.gender"), this.font), genderDropDownList);
    }

    private void sendApplyPacket() {
        if (selectedEggIndex == -1) {
            showStatus(Component.literal("No egg selected"), 40, false);
            return;
        }

        Pokemon egg = eggs.get(selectedEggIndex);

        // Packet an Server senden
        PacketDistributor.sendToServer(new LarielBetterBreedingApplyPacket(
                egg.getUUID(),
                selectedForm,
                selectedPalette,
                selectedGender
        ));
    }
}
