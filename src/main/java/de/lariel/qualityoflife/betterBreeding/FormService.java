package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import de.lariel.qualityoflife.config.LarielsQoLConfig;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FormService {

    private final LarielsQolBetterBreedingConfig config;
    private final Logger logger;

    public FormService(LarielsQolBetterBreedingConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void applyFormInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        var chance = config.getFormInheritanceChance();
        var random = Math.random();

        logger.info("Form-Inheritance Chance: {}\tRandom-Value: {}\tApply Form: {}", chance, random, random < chance);
        if (random >= chance)
            return;

        var palette1 = safePaletteName(p1);
        var palette2 = safePaletteName(p2);

        var supported = getSupportedPalettes(egg);

        var supportsP1 = supported.contains(palette1);
        var supportsP2 = supported.contains(palette2);

        if (supportsP1 && supportsP2) {
            var palette = Math.random() < 0.5 ? palette1 : palette2;
            logger.info("Form-Inheritance: Updated Form to {}",  palette);
            egg.setPalette(palette);
            return;
        }

        if (supportsP1) {
            logger.info("Form-Inheritance: Updated Form to {}",  palette1);
            egg.setPalette(palette1);
            return;
        }

        if (supportsP2) {
            logger.info("Form-Inheritance: Updated Form to {}",  palette2);
            egg.setPalette(palette2);
        }
    }

    private String safePaletteName(Pokemon p) {
        return p != null && p.getPalette() != null
                ? p.getPalette().getName()
                : "none";
    }

    private Set<String> getSupportedPalettes(Pokemon egg) {

        if (egg == null || egg.getGenderProperties() == null)
            return Set.of();

        return Arrays.stream(Objects.requireNonNull(egg.getGenderProperties()).getPalettes())
                .map(PaletteProperties::getName)
                .collect(Collectors.toSet());
    }
}