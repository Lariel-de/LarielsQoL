package de.lariel.qualityoflife.client.screen;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBase;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.gui.npc.widget.DropDownWidget;
import com.pixelmonmod.pixelmon.client.gui.npc.widget.ScrollableListWidget;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.businessLogic.LarielPreviewFactory;
import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielCostService;
import de.lariel.qualityoflife.client.screen.services.LarielDropDownFactory;
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

@SuppressWarnings({"rawtypes", "unchecked"})
public class LarielBetterBreedingScreen extends Screen {

    private static final int PANEL_HEIGHT = 180;
    private static final int LEFT_PANEL_WIDTH = 120;
    private static final int RIGHT_PANEL_WIDTH = 200;
    private static final int PANEL_SPACING = 20;

    private final List<Pokemon> eggs = new ArrayList<>();
    private final LarielsQolBetterBreedingConfig config;

    private int selectedEggIndex = -1;

    private Component statusMessage = null;
    private long statusUntil = 0;
    private int statusColor = 0xFFFFFFFF;

    private String selectedForm;
    private String selectedPalette;
    private String selectedGender;

    private ScrollableListWidget editorList;
    private Pokemon preview;
    private Pokemon egg;
    private Button applyButton;

    public LarielBetterBreedingScreen() {
        super(Component.translatable("betterbreeding.larielsqualityoflife.title"));
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

        var storage = StorageProxy.getPartyNow(player.getUUID());
        if (storage == null) return;

        var party = storage.getTeam(PokemonBase::isEgg);
        if (party == null) return;

        eggs.addAll(party);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gfx, mouseX, mouseY, partialTicks);

        gfx.drawString(this.font, Component.translatable("betterbreeding.larielsqualityoflife.title").getString(), this.width / 2 - 50, 20, 0xFFFFFF);

        var totalWidth = LEFT_PANEL_WIDTH + PANEL_SPACING + RIGHT_PANEL_WIDTH;
        var startX = (this.width - totalWidth) / 2;

        var rightX = startX + LEFT_PANEL_WIDTH + PANEL_SPACING;
        var y = 50;

        renderLeftPanel(gfx, startX, y);
        renderRightPanel(gfx, rightX, y);

        super.render(gfx, mouseX, mouseY, partialTicks);
        renderStatusMessage(gfx);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, 0xAA000000);
    }

    private void renderLeftPanel(GuiGraphics gfx, int x, int y) {
        gfx.fill(x, y, x + LEFT_PANEL_WIDTH, y + PANEL_HEIGHT, 0xAA000000);
        gfx.drawString(this.font, Component.translatable("betterbreeding.larielsqualityoflife.eggs").getString(), x + 6, y + 6, 0xFFFFFF);

        var entryY = y + 20;

        for (var i = 0; i < eggs.size(); i++) {
            var egg = eggs.get(i);
            var color = (i == selectedEggIndex) ? 0xFF55FF55 : 0xFFFFFFFF;

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
                    egg.getSpecies().getTranslatedName().getString(),
                    x + 24,
                    entryY,
                    color
            );

            entryY += 12;
        }
    }

    private void renderRightPanel(GuiGraphics gfx, int x, int y) {
        gfx.fill(x, y, x + RIGHT_PANEL_WIDTH, y + PANEL_HEIGHT, 0xAA000000);
        gfx.drawString(this.font, "Editor", x + 6, y + 6, 0xFFFFFF);

        if (selectedEggIndex == -1 || egg == null || preview == null) {
            gfx.drawString(this.font, Component.translatable("betterbreeding.larielsqualityoflife.selectegg"), x + 6, y + 30, 0xAAAAAA);
            return;
        }

        gfx.drawString(this.font, Component.translatable("betterbreeding.larielsqualityoflife.species").getString() + ": " +
                egg.getSpecies().getTranslatedName().getString(), x + 6, y + 30, 0xFFFFFF);

        var spriteX = x + RIGHT_PANEL_WIDTH - 40;
        var spriteY = y + 10;

        applyPreviewSelections();

        ScreenHelper.drawImageQuad(
                preview.getSprite(),
                gfx,
                spriteX,
                spriteY,
                32, 32,
                0, 0, 1, 1,
                1
        );

        var costX = spriteX - 50;
        var costY = y + 65;

        var formCosts = LarielCostService.CalculateFormCosts(egg, selectedForm);
        if (formCosts != null) {
            renderCost(gfx, costX, costY, formCosts.item(), formCosts.amount());
        }

        var paletteCosts = LarielCostService.CalculatePaletteCosts(egg, selectedPalette);
        if (paletteCosts != null) {
            renderCost(gfx, costX, costY + 30, paletteCosts.item(), paletteCosts.amount());
        }

        var genderCosts = LarielCostService.CalculateGenderCosts(egg, selectedGender);
        if (genderCosts != null) {
            renderCost(gfx, costX, costY + 60, genderCosts.item(), genderCosts.amount());
        }

        updateApplyButtonState();
    }

    private void applyPreviewSelections() {
        if (preview == null) return;

        if (selectedForm != null) {
            preview.setForm(selectedForm);
        }

        if (selectedPalette != null) {
            preview.setPalette(selectedPalette);
        }

        if (selectedGender != null) {
            preview.setGender(Gender.valueOf(selectedGender));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var totalWidth = LEFT_PANEL_WIDTH + PANEL_SPACING + RIGHT_PANEL_WIDTH;
        var startX = (this.width - totalWidth) / 2;
        var y = 50;

        if (mouseX >= startX && mouseX <= startX + LEFT_PANEL_WIDTH &&
                mouseY >= y + 20 && mouseY <= y + PANEL_HEIGHT) {

            var index = (int) ((mouseY - (y + 20)) / 12);

            if (index >= 0 && index < eggs.size()) {
                selectedEggIndex = index;
                setupDropdowns();
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderStatusMessage(GuiGraphics gfx) {
        if (statusMessage != null && System.currentTimeMillis() < statusUntil) {
            var x = (this.width / 2 - this.font.width(statusMessage) / 2) + 90;
            var y = this.height - 40;
            gfx.drawString(this.font, statusMessage, x, y, statusColor);
        }
    }

    private void renderCost(GuiGraphics gfx, int x, int y, Item item, int amount) {
        if (amount <= 0) return;

        gfx.renderItem(new ItemStack(item), x, y);
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

        preview = LarielPreviewFactory.createEggPreview(egg);

        if (editorList != null) {
            removeWidget(editorList);
        }

        editorList = initEditorList();

        createFormDropDown(editorList, egg);
        createPaletteDropDown(editorList, egg);
        createGenderDropDown(editorList, egg);

        this.applyButton = Button.builder(Component.translatable("betterbreeding.larielsqualityoflife.apply"),
                        btn -> sendApplyPacket())
                .size(90, 20)
                .build();

        applyButton.active = false;

        editorList.addWidgets(applyButton);
        addRenderableWidget(editorList);
    }

    private @NotNull ScrollableListWidget<? extends LayoutElement> initEditorList() {
        return new ScrollableListWidget<>(
                this.width / 2 - 28,
                100,
                RIGHT_PANEL_WIDTH,
                PANEL_HEIGHT
        );
    }

    private void rebuildDependentDropdowns() {
        if (preview == null || editorList == null) return;

        var genderProps = preview.getForm().getGenderProperties(preview.getGender());
        List<PaletteProperties> palettes = genderProps != null
                ? Lists.newArrayList(genderProps.getPalettes())
                : new ArrayList<>();

        if (!config.getShinyGuaranteeEnabled()) {
            palettes.removeIf(p -> p.getName().toLowerCase().contains("shiny"));
        }

        List<DropDownWidget> dropdowns = editorList.getWidgets().stream()
                .filter(w -> w instanceof DropDownWidget<?>)
                .toList();

        if (dropdowns.size() < 3) return;

        var paletteDropDown = dropdowns.get(1);
        var genderDropDown = dropdowns.get(2);

        paletteDropDown.setOptions(palettes, preview.getPalette());

        var genders = preview.getForm().getPossibleGenders();
        genderDropDown.setOptions(genders, preview.getGender());

        if (paletteDropDown.getSelected() instanceof PaletteProperties palette)
            selectedPalette = palette.getName();

        if (genderDropDown.getSelected() instanceof Gender gender)
            selectedGender = gender.name();
    }

    private void createFormDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        if (!config.getFormGuaranteeEnabled())
            return;

        var forms = egg.getSpecies().getForms(true);
        var formDropDown = LarielDropDownFactory.create(forms, egg.getForm(),
                p -> Component.translatable(p.getTranslationKey()).getString(),
                p -> {
                    selectedForm = p.getName();
                    preview.setForm(selectedForm);
                    rebuildDependentDropdowns();
                });

        if (formDropDown.getSelected() != null) {
            selectedForm = formDropDown.getSelected().getName();
        }

        scrollableListWidget.addWidgets(
                new StringWidget(Component.translatable("gui.pokemoneditor.form"), this.font),
                formDropDown
        );
    }

    private void createPaletteDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        if (!config.getPaletteGuaranteeEnabled())
            return;

        var genderProps = egg.getForm().getGenderProperties(egg.getGender());
        List<PaletteProperties> palettes = genderProps != null
                ? Lists.newArrayList(genderProps.getPalettes())
                : new ArrayList<>();

        if (!config.getShinyGuaranteeEnabled()) {
            palettes.removeIf(p -> p.getName().toLowerCase().contains("shiny"));
        }

        var paletteDropDownList = LarielDropDownFactory.create(palettes, egg.getPalette(),
                p -> Component.translatable(p.getTranslationKey()).getString(),
                p -> selectedPalette = p.getName());
        if (paletteDropDownList.getSelected() != null) {
            selectedPalette = paletteDropDownList.getSelected().getName();
            preview.setPalette(selectedPalette);
        }

        scrollableListWidget.addWidgets(
                new StringWidget(Component.translatable("gui.pokemoneditor.palette"), this.font),
                paletteDropDownList
        );
    }

    private void createGenderDropDown(ScrollableListWidget scrollableListWidget, Pokemon egg) {
        if (!config.getGenderGuaranteeEnabled())
            return;

        List<Gender> genders = Lists.newArrayList(egg.getForm().getPossibleGenders());

        if (config.getAllowForceMaleFemaleGender()) {
            addMissingGender(genders);
        }

        var genderDropDownList = LarielDropDownFactory.create(genders, egg.getGender(),
                p -> Component.translatable(p.getTranslationKey()).getString(),
                p -> selectedGender = p.name());

        if (genderDropDownList.getSelected() != null) {
            selectedGender = genderDropDownList.getSelected().name();
            preview.setGender(genderDropDownList.getSelected());
        }

        scrollableListWidget.addWidgets(
                new StringWidget(Component.translatable("gui.pokemoneditor.gender"), this.font),
                genderDropDownList
        );
    }

    private static void addMissingGender(List<Gender> genders) {
        var male = Gender.getGender("male");
        var female = Gender.getGender("female");

        if (male == null || female == null)
            return;

        if (!genders.contains(male))
            genders.add(male);
        if (!genders.contains(female))
            genders.add(female);
    }

    private void updateApplyButtonState() {
        if (applyButton == null || selectedForm == null || selectedPalette == null || selectedGender == null) return;

        applyButton.active = !selectedForm.equals(egg.getForm().getName()) ||
                !selectedPalette.equals(egg.getPalette().getName()) ||
                !selectedGender.equals(egg.getGender().name());
    }


    private void sendApplyPacket() {
        if (selectedEggIndex == -1 || egg == null) {
            showStatus(Component.literal("No egg selected"), 40, false);
            return;
        }

        PacketDistributor.sendToServer(new LarielBetterBreedingApplyPacket(
                egg.getUUID(),
                selectedForm,
                selectedPalette,
                selectedGender
        ));
    }
}