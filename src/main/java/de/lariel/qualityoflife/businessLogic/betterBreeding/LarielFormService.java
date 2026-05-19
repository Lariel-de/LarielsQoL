package de.lariel.qualityoflife.businessLogic.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

public class LarielFormService {

    private final LarielsQolBetterBreedingConfig config;
    private final Logger logger;

    public LarielFormService(LarielsQolBetterBreedingConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void applyFormInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        var chance = config.getFormInheritanceChance();
        var random = Math.random();

        logger.info("Form-Inheritance Chance: {}\tRandom-Value: {}\tApply Form: {}", chance, random, random < chance);
        if (random >= chance)
            return;

        var form1 = safeFormName(p1);
        var form2 = safeFormName(p2);

        var supported = getSupportedForms(egg);

        var supportsP1 = supported.contains(form1);
        var supportsP2 = supported.contains(form2);

        if (supportsP1 && supportsP2) {
            var palette = Math.random() < 0.5 ? form1 : form2;
            logger.info("Form-Inheritance: Updated Form to {}", palette);
            egg.setForm(palette);
            return;
        }

        if (supportsP1) {
            logger.info("Form-Inheritance: Updated Form to {}", form1);
            egg.setForm(form1);
            return;
        }

        if (supportsP2) {
            logger.info("Form-Inheritance: Updated Form to {}", form2);
            egg.setForm(form2);
        }
    }

    private String safeFormName(Pokemon p) {
        return p != null && p.getForm() != null
                ? p.getForm().getName()
                : "none";
    }

    private Set<String> getSupportedForms(Pokemon egg) {

        if (egg == null || egg.getGenderProperties() == null)
            return Set.of();

        return egg.getSpecies()
                .getForms()
                .stream()
                .map(Stats::getName)
                .collect(Collectors.toSet());
    }
}