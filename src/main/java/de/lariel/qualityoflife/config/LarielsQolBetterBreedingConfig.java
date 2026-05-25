package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import info.pixelmon.repack.org.spongepowered.objectmapping.meta.Comment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/BetterBreeding.yml")
public class LarielsQolBetterBreedingConfig extends AbstractYamlConfig {
    // --- BetterBreeding: Undiscovered Breeding ---
    @Comment("Is Undiscovered Breeding allowed at all\n")
    private final boolean allowUndiscoveredBreeding;
    @Comment("How many times a player has to breed something, until undiscovered breeding is unlocked (if it's enabled)\n")
    private final int undiscoveredBreedingCount;
    @Comment("How many Baby Pokémon the player has to breed, until undiscovered breeding is unlocked (if it's enabled)\n")
    private final int undiscoveredBabyBreedingCount;

    // --- BetterBreeding: IV Inheritance ---
    @Comment("Chance that the better IV from one of the parents is inherited (enter \"0\" for disabling)\n")
    private final double ivInheritanceChance;

    // --- BetterBreeding: Shiny ---
    @Comment("Base chance (if both parents aren't shiny) that the child is shiny (0 = disabled, 100 = all childs are shiny)\n")
    private final double shinyBaseChance;
    @Comment("Multiplier for each shiny parent\nBaseChance * shinyParentMultiplier * shinyParentMultiplier if both parents are shiny")
    private final double shinyParentMultiplier;
    @Comment("Setting if the Better Breeding NPC gives the option to force a shiny.\n")
    private final boolean shinyGuaranteeEnabled;
    @Comment("How many items the player has to pay for guaranteed shiny.\n")
    private final int shinyGuaranteeCostAmount;
    @Comment("What item the player has to pay for guaranteed shiny.\n")
    private final String shinyGuaranteeCostItem;

    // --- BetterBreeding: Form/Skin ---
    @Comment("Chance that one of the special forms of the parents is inherited\n(if both parents have a special form the first parent always wins)\n")
    private final double formInheritanceChance;
    @Comment("Setting whether the Better Breeding NPC allow to change the Form.\n")
    private final boolean formGuaranteeEnabled;
    @Comment("How many items the player has to pay for guaranteed form.\n")
    private final int formGuaranteeCostAmount;
    @Comment("What item the player has to pay for guaranteed form.\n")
    private final String formGuaranteeCostItem;

    // --- BetterBreeding: Palette/Skin ---
    @Comment("Setting whether the Better Breeding NPC allow to change the Palette.\n")
    private final boolean paletteGuaranteeEnabled;
    @Comment("How many items the player has to pay for guaranteed palette.\n")
    private final int paletteGuaranteeCostAmount;
    @Comment("What item the player has to pay for guaranteed palette.\n")
    private final String paletteGuaranteeCostItem;

    // --- BetterBreeding: Gender ---
    @Comment("Setting whether the Better Breeding NPC allow to change the Gender.\n")
    private final boolean genderGuaranteeEnabled;
    @Comment("How many items the player has to pay for guaranteed gender.\n")
    private final int genderGuaranteeCostAmount;
    @Comment("What item the player has to pay for guaranteed gender.\n")
    private final String genderGuaranteeCostItem;
    @Comment("Setting whether players can force illegal genders (e.g. M/F for legendaries, female hitmons)\n")
    private final boolean allowForceMaleFemaleGender;

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
        this.allowForceMaleFemaleGender = false;
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

    public boolean getAllowForceMaleFemaleGender() {
        return allowForceMaleFemaleGender;
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
