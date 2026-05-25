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
    @Comment("Defines the range of the spawn notifications\n")
    private final double spawnDetectionRadius;

    public LarielsQoLConfig() {
        super();

        // --- QoL Defaults ---
        this.enableSpawnNotification = true;
        this.spawnDetectionRadius = 90;
    }

    // --- Getter: QoL ---
    public boolean getEnableSpawnNotification() {
        return enableSpawnNotification;
    }

    public double getSpawnDetectionRadius() {
        return spawnDetectionRadius;
    }
}