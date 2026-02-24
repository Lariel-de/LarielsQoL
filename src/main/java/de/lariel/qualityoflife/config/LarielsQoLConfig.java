package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/ModId/config.yml")
public class LarielsQoLConfig extends AbstractYamlConfig {
    private final boolean _enableSpawnNotification;

    public LarielsQoLConfig() {
        super();

        _enableSpawnNotification = true;
    }

    public boolean getEnableSpawnNotificationField() {
        return _enableSpawnNotification;
    }
}
