package de.lariel.qualityoflife.config;

import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import org.apache.logging.log4j.Logger;

public class LarielsQolConfigManager {

    private final Logger logger;
    private LarielsQoLConfig general;
    private LarielsQolBetterBreedingConfig breeding;
    private LarielsQolEnchantmentConfig enchantments;

    public LarielsQolConfigManager(Logger logger) {
        this.logger = logger;
    }

    public void loadAll() {
        try {
            general = YamlConfigFactory.getInstance(LarielsQoLConfig.class);
            breeding = YamlConfigFactory.getInstance(LarielsQolBetterBreedingConfig.class);
            enchantments = YamlConfigFactory.getInstance(LarielsQolEnchantmentConfig.class);
        } catch (Exception e) {
            logger.error("Failed to load configs", e);
        }
    }

    public LarielsQoLConfig general() {
        return general;
    }

    public LarielsQolBetterBreedingConfig breeding() {
        return breeding;
    }

    public LarielsQolEnchantmentConfig enchantments() {
        return enchantments;
    }
}
