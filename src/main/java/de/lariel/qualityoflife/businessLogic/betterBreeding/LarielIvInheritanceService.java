package de.lariel.qualityoflife.businessLogic.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import org.apache.logging.log4j.Logger;

public class LarielIvInheritanceService {

    private final LarielsQolBetterBreedingConfig config;
    private final Logger logger;

    public LarielIvInheritanceService(LarielsQolBetterBreedingConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void applyIvInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        var chance = config.getIvInheritanceChance();

        for (var stat : BattleStatsType.values()) {
            var random = Math.random();
            logger.info("IV-Inheritance: Chance: {}\tRandom-Value: {}\tRandom < Chance: {}", chance, random, random < chance);
            if (random < chance) {
                var iv1 = p1.getIVs().getStat(stat);
                var iv2 = p2.getIVs().getStat(stat);
                var maxIV = Math.max(iv1, iv2);
                logger.info("IV-Inheritance: Changed {} from {} to {}", stat.name(), egg.getIVs().getStat(stat), maxIV);
                egg.getIVs().setStat(stat, maxIV);
            }
        }
    }
}