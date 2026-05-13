package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import de.lariel.qualityoflife.config.LarielsQoLConfig;

public class IvInheritanceService {

    private final LarielsQoLConfig config;

    public IvInheritanceService(LarielsQoLConfig config) {
        this.config = config;
    }

    public void applyIvInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        double chance = config.getIvInheritanceChance();

        for (var stat : BattleStatsType.values()) {

            if (Math.random() < chance) {
                int iv1 = p1.getIVs().getStat(stat);
                int iv2 = p2.getIVs().getStat(stat);
                egg.getIVs().setStat(stat, Math.max(iv1, iv2));
            }
        }
    }
}