package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import org.apache.logging.log4j.Logger;

public class LarielShinyService {

    private final LarielsQolBetterBreedingConfig config;
    private final Logger logger;

    public LarielShinyService(LarielsQolBetterBreedingConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void applyShinyLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        var chance = config.getShinyBaseChance();
        var random = Math.random();

        var shinyParents = (p1.isShiny() ? 1 : 0) + (p2.isShiny() ? 1 : 0);

        for (var i = 0; i < shinyParents; i++) {
            chance *= config.getShinyParentMultiplier();
        }

        logger.info("Shiny-Inheritance Chance: {}\tRandom-Value: {}\tApply shiny: {}", chance, random, random < chance);
        if (random < chance) {
            egg.setShiny(true);
        }
    }
}