package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/BetterBreeding.yml")
public class LarielsQolBetterBreedingConfig extends AbstractYamlConfig {
    // --- BetterBreeding: Undiscovered Breeding ---
    private final boolean allowUndiscoveredBreeding;
    private final int undiscoveredBreedingCount;
    private final int undiscoveredBabyBreedingCount;

    // --- BetterBreeding: IV Inheritance ---
    private final double ivInheritanceChance;

    // --- BetterBreeding: Shiny ---
    private final double shinyBaseChance;
    private final double shinyParentMultiplier;
    private final boolean shinyGuaranteeEnabled;
    private final int shinyGuaranteeCostAmount;
    private final String shinyGuaranteeCostItem;

    // --- BetterBreeding: Form/Skin ---
    private final double formInheritanceChance;
    private final boolean formGuaranteeEnabled;
    private final int formGuaranteeCostAmount;
    private final String formGuaranteeCostItem;

    // --- BetterBreeding: Palette/Skin ---
    private final boolean paletteGuaranteeEnabled;
    private final int paletteGuaranteeCostAmount;
    private final String paletteGuaranteeCostItem;

    // --- BetterBreeding: Gender ---
    private final boolean genderGuaranteeEnabled;
    private final int genderGuaranteeCostAmount;
    private final String genderGuaranteeCostItem;

    public LarielsQolBetterBreedingConfig() {
        // --- Undiscovered Breeding Defaults ---
        this.allowUndiscoveredBreeding = true;
        this.undiscoveredBreedingCount = 20;
        this.undiscoveredBabyBreedingCount = 3;

        // --- IV Inheritance Defaults ---
        this.ivInheritanceChance = 0.9;

        // --- Shiny Defaults ---
        this.shinyBaseChance = 0.01;
        this.shinyParentMultiplier = 1.5;
        this.shinyGuaranteeEnabled = true;
        this.shinyGuaranteeCostAmount = 32;
        this.shinyGuaranteeCostItem = "minecraft:emerald";

        // --- Form/Skin Defaults ---
        this.formInheritanceChance = 0.9;
        this.formGuaranteeEnabled = true;
        this.formGuaranteeCostAmount = 5;
        this.formGuaranteeCostItem = "minecraft:emerald";

        // --- Form/Skin Defaults ---
        this.paletteGuaranteeEnabled = true;
        this.paletteGuaranteeCostAmount = 3;
        this.paletteGuaranteeCostItem = "minecraft:emerald";

        // --- Gender Defaults ---
        this.genderGuaranteeEnabled = true;
        this.genderGuaranteeCostAmount = 2;
        this.genderGuaranteeCostItem = "minecraft:emerald";
    }

    // --- Getter: Undiscovered Breeding ---
    public boolean getAllowUndiscoveredBreeding() {
        return allowUndiscoveredBreeding;
    }

    public int getUndiscoveredBreedingCount() {
        return undiscoveredBreedingCount;
    }

    public int getUndiscoveredBabyBreedingCount() {
        return undiscoveredBabyBreedingCount;
    }

    // --- Getter: IV Inheritance ---
    public double getIvInheritanceChance() {
        return ivInheritanceChance;
    }

    // --- Getter: Shiny ---
    public double getShinyBaseChance() {
        return shinyBaseChance;
    }

    public double getShinyParentMultiplier() {
        return shinyParentMultiplier;
    }

    public boolean getShinyGuaranteeEnabled() {
        return shinyGuaranteeEnabled;
    }

    public int getShinyGuaranteeCostAmount() {
        return shinyGuaranteeCostAmount;
    }

    public Item getShinyGuaranteeCostItem() {
        return getCostItem(shinyGuaranteeCostItem);
    }

    // --- Getter: Form/Skin ---
    public double getFormInheritanceChance() {
        return formInheritanceChance;
    }

    public boolean getFormGuaranteeEnabled() {
        return formGuaranteeEnabled;
    }

    public int getFormGuaranteeCostAmount() {
        return formGuaranteeCostAmount;
    }

    public Item getFormGuaranteeCostItem() {
        return getCostItem(formGuaranteeCostItem);
    }

    // --- Getter: Palette/Skin ---
    public boolean getPaletteGuaranteeEnabled() {
        return paletteGuaranteeEnabled;
    }

    public int getPaletteGuaranteeCostAmount() {
        return paletteGuaranteeCostAmount;
    }

    public Item getPaletteGuaranteeCostItem() {
        return getCostItem(paletteGuaranteeCostItem);
    }

    // --- Getter: Gender ---
    public boolean getGenderGuaranteeEnabled() {
        return genderGuaranteeEnabled;
    }

    public int getGenderGuaranteeCostAmount() {
        return genderGuaranteeCostAmount;
    }

    public Item getGenderGuaranteeCostItem() {
        return getCostItem(genderGuaranteeCostItem);
    }

    private Item getCostItem(String itemString) {
        var parts = itemString.split(":");
        var id = ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]);
        var item = BuiltInRegistries.ITEM.get(id);

        if (item == Items.AIR) {
            LarielsQoL.LOGGER.error("Invalid cost item: {}", itemString);
        }

        return item;
    }
}
