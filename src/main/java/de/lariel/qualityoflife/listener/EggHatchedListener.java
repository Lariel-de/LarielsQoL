package de.lariel.qualityoflife.listener;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.config.LarielsQoLConfig;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EggHatchedListener {

    private final LarielsQoLConfig config;

    public EggHatchedListener() {
        this.config = LarielsQoL.getConfig();
    }

    @SubscribeEvent
    public void onPreEggCollect(DayCareEvent.PreCollect event) {

        Pokemon parent1 = event.getParentOne();
        Pokemon parent2 = event.getParentTwo();
        Pokemon egg = event.getChildGiven();

        if (parent1 == null || parent2 == null || egg == null) {
            return;
        }

        applyIvInheritance(egg, parent1, parent2);
        applyShinyLogic(egg, parent1, parent2);
        applyBaseFormLogic(egg, parent1, parent2);
    }

    // ------------------------------------------------------------
    //  IV INHERITANCE
    // ------------------------------------------------------------

    private void applyIvInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        double chance = config.GetIvInheritanceChance();

        for (BattleStatsType stat : BattleStatsType.values()) {

            if (Math.random() < chance) {
                int iv1 = p1.getIVs().getStat(stat);
                int iv2 = p2.getIVs().getStat(stat);
                egg.getIVs().setStat(stat, Math.max(iv1, iv2));
            }
        }
    }

    // ------------------------------------------------------------
    //  SHINY LOGIC
    // ------------------------------------------------------------

    private void applyShinyLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        double shinyChance = config.GetShinyBaseChance();

        int shinyParents = 0;
        if (p1.isShiny()) shinyParents++;
        if (p2.isShiny()) shinyParents++;

        for (int i = 0; i < shinyParents; i++) {
            shinyChance *= config.GetShinyParentMultiplier();
        }

        if (Math.random() < shinyChance) {
            egg.setShiny(true);
        }
    }

    // ------------------------------------------------------------
    //  FORM / PALETTE LOGIC
    // ------------------------------------------------------------

    private void applyBaseFormLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        // 1. Chance prüfen
        if (Math.random() >= config.GetFormInheritanceChance()) {
            return;
        }

        // 2. Paletten der Eltern
        String palette1 = safePaletteName(p1);
        String palette2 = safePaletteName(p2);

        // 3. Unterstützte Paletten des Eis
        Set<String> eggSupported = getSupportedPalettes(egg);

        boolean supportsP1 = eggSupported.contains(palette1);
        boolean supportsP2 = eggSupported.contains(palette2);

        // 4. Beide kompatibel → 50:50
        if (supportsP1 && supportsP2) {
            egg.setPalette(Math.random() < 0.5 ? palette1 : palette2);
            return;
        }

        // 5. Nur P1 kompatibel
        if (supportsP1) {
            egg.setPalette(palette1);
            return;
        }

        // 6. Nur P2 kompatibel
        if (supportsP2) {
            egg.setPalette(palette2);
        }
    }

    // ------------------------------------------------------------
    //  UTILITY METHODS
    // ------------------------------------------------------------

    private String safePaletteName(Pokemon p) {
        return p != null && p.getPalette() != null
                ? p.getPalette().getName()
                : "none";
    }

    private Set<String> getSupportedPalettes(Pokemon egg) {

        if (egg == null || egg.getGenderProperties() == null) {
            return Set.of();
        }

        return Arrays.stream(Objects.requireNonNull(egg.getGenderProperties()).getPalettes())
                .map(PaletteProperties::getName)
                .collect(Collectors.toSet());
    }
}