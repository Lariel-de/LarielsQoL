package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/config.yml")
public class LarielsQoLConfig extends AbstractYamlConfig {

    // --- General QoL Settings ---
    private final boolean enableSpawnNotification;
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