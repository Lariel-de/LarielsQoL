package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/Enchantments.yml")
public class LarielsQolEnchantmentConfig extends AbstractYamlConfig {
    private final double warmStepsMultiplier;

    public LarielsQolEnchantmentConfig() {
        this.warmStepsMultiplier = 0.1;
    }

    public double getWarmStepsMultiplier() {
        return warmStepsMultiplier;
    }
}
