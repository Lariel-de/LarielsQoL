package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

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
        this.shinyGuaranteeCostAmount = 10;
        this.shinyGuaranteeCostItem = "minecraft:emerald";

        // --- Form/Skin Defaults ---
        this.formInheritanceChance = 0.9;
        this.formGuaranteeEnabled = true;
        this.formGuaranteeCostAmount = 5;
        this.formGuaranteeCostItem = "minecraft:emerald";
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

    public String getShinyGuaranteeCostItem() {
        return shinyGuaranteeCostItem;
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

    public String getFormGuaranteeCostItem() {
        return formGuaranteeCostItem;
    }
}
