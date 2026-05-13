package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import de.lariel.qualityoflife.config.LarielsQoLConfig;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FormService {

    private final LarielsQoLConfig config;

    public FormService(LarielsQoLConfig config) {
        this.config = config;
    }

    public void applyFormInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        if (Math.random() >= config.getFormInheritanceChance())
            return;

        String palette1 = safePaletteName(p1);
        String palette2 = safePaletteName(p2);

        Set<String> supported = getSupportedPalettes(egg);

        boolean supportsP1 = supported.contains(palette1);
        boolean supportsP2 = supported.contains(palette2);

        if (supportsP1 && supportsP2) {
            egg.setPalette(Math.random() < 0.5 ? palette1 : palette2);
            return;
        }

        if (supportsP1) {
            egg.setPalette(palette1);
            return;
        }

        if (supportsP2) {
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