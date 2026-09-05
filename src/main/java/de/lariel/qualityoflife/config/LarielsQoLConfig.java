package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import info.pixelmon.repack.org.spongepowered.objectmapping.meta.Comment;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/config.yml")
public class LarielsQoLConfig extends AbstractYamlConfig {

    // --- General QoL Settings ---
    @Comment("Are spawn notifications enabled\n")
    private final boolean enableSpawnNotification;
    @Comment("Should wild Pokemon levels be raised based on the spawning player's party level when lower\n")
    private final boolean enableSpawnLevelAdjustment;
    @Comment("Maximum level for adjusted wild Pokemon spawns in the Overworld\n")
    private final int overworldSpawnMaxLevel;
    @Comment("Defines the range of the spawn notifications\n")
    private final double spawnDetectionRadius;

    @Comment("Defines the max level of a lariel shopkeeper\n")
    private final int shopkeeperMaxLevel;
    @Comment("Higher values = more XP required to level up at a shopkeeper\n")
    private final double levelScaleFactor;

    public LarielsQoLConfig() {
        super();

        // --- QoL Defaults ---
        this.enableSpawnNotification = true;
        this.enableSpawnLevelAdjustment = true;
        this.overworldSpawnMaxLevel = 60;
        this.spawnDetectionRadius = 90;
        shopkeeperMaxLevel = 5;
        levelScaleFactor = 5.0;
    }

    // --- Getter: QoL ---
    public boolean getEnableSpawnNotification() {
        return enableSpawnNotification;
    }

    public boolean getEnableSpawnLevelAdjustment() {
        return enableSpawnLevelAdjustment;
    }

    public int getOverworldSpawnMaxLevel() {
        return overworldSpawnMaxLevel;
    }

    public double getSpawnDetectionRadius() {
        return spawnDetectionRadius;
    }

    public int getShopkeeperMaxLevel() {
        return shopkeeperMaxLevel;
    }

    public double getLevelScaleFactor() {
        return levelScaleFactor;
    }
}