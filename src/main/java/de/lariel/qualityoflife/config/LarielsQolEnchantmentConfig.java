package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import de.lariel.qualityoflife.LarielsQoL;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import info.pixelmon.repack.org.spongepowered.objectmapping.meta.Comment;

@ConfigSerializable
@ConfigPath("config/" + LarielsQoL.MOD_ID + "/Enchantments.yml")
public class LarielsQolEnchantmentConfig extends AbstractYamlConfig {
    @Comment("""
            How many % of the egg cycles should be reduced with enchantment.
            e.g. 0.1 = 10% reduction, 0.5 = 50% reduction, 1.0 = 100% reduction = egg is breed after one step.
            """)
    private final double warmStepsMultiplier;

    public LarielsQolEnchantmentConfig() {
        this.warmStepsMultiplier = 0.1;
    }

    public double getWarmStepsMultiplier() {
        return warmStepsMultiplier;
    }
}
