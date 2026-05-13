package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.lariel.qualityoflife.config.LarielsQoLConfig;

public class ShinyService {

    private final LarielsQoLConfig config;

    public ShinyService(LarielsQoLConfig config) {
        this.config = config;
    }

    public void applyShinyLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        double shinyChance = config.getShinyBaseChance();

        int shinyParents = (p1.isShiny() ? 1 : 0) + (p2.isShiny() ? 1 : 0);

        for (int i = 0; i < shinyParents; i++) {
            shinyChance *= config.getShinyParentMultiplier();
        }

        if (Math.random() < shinyChance) {
            egg.setShiny(true);
        }
    }
}