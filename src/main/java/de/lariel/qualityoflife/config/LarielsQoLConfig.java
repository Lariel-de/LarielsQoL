package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/config.yml")
public class LarielsQoLConfig extends AbstractYamlConfig {

    // --- General QoL-Settings ---
    private boolean _enableSpawnNotification;
    private double _spawnDetectionRadius;

    // --- BetterBreeding: IV-Inheritance ---
    private double _ivInheritanceChance = 0.9; // Chance pro Stat, dass der höhere IV übernommen wird

    // --- BetterBreeding: Shiny ---
    private double _shinyBaseChance = 0.01; // Basis-Shiny-Chance
    private double _shinyParentMultiplier = 1.5; // 50% Bonus pro Shiny-Elternteil
    private boolean _shinyGuaranteeEnabled = true;
    private int _shinyGuaranteeCostAmount = 10;
    private String _shinyGuaranteeCostItem = "minecraft:emerald";

    // --- BetterBreeding: Form/Skin ---
    private double _formInheritanceChance = 0.9; // 90% Chance
    private boolean _formGuaranteeEnabled = true;
    private int _formGuaranteeCostAmount = 5;
    private String _formGuaranteeCostItem = "minecraft:emerald";

    public LarielsQoLConfig() {
        super();

        _enableSpawnNotification = true;
        _spawnDetectionRadius = 90;
    }

    // --- Getter: QoL ---
    public boolean GetEnableSpawnNotificationField() {
        return _enableSpawnNotification;
    }

    public double GetSpawnDetectionRadiusField() {
        return _spawnDetectionRadius;
    }


    // --- Getter: IV-Vererbung ---
    public double GetIvInheritanceChance() {
        return _ivInheritanceChance;
    }


    // --- Getter: Shiny ---
    public double GetShinyBaseChance() {
        return _shinyBaseChance;
    }

    public double GetShinyParentMultiplier() {
        return _shinyParentMultiplier;
    }

    public boolean GetShinyGuaranteeEnabled() {
        return _shinyGuaranteeEnabled;
    }

    public int GetShinyGuaranteeCostAmount() {
        return _shinyGuaranteeCostAmount;
    }

    public String GetShinyGuaranteeCostItem() {
        return _shinyGuaranteeCostItem;
    }

    // --- Getter: Form/Skin ---
    public double GetFormInheritanceChance() {
        return _formInheritanceChance;
    }

    public boolean GetFormGuaranteeEnabled() {
        return _formGuaranteeEnabled;
    }

    public int GetFormGuaranteeCostAmount() {
        return _formGuaranteeCostAmount;
    }

    public String GetFormGuaranteeCostItem() {
        return _formGuaranteeCostItem;
    }
}
