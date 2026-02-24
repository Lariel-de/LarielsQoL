package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/config.yml")
public class LarielsQoLConfig extends AbstractYamlConfig {
    private final boolean _enableSpawnNotification;
    private final double _spawnDetectionRadius;

    public LarielsQoLConfig() {
        super();

        _enableSpawnNotification = true;
        _spawnDetectionRadius = 90;
    }

    public boolean GetEnableSpawnNotificationField() {
        return _enableSpawnNotification;
    }

    public double GetSpawnDetectionRadiusField() {
        return _spawnDetectionRadius;
    }
}
