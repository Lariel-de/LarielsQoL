package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.palette.PaletteProperties;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.config.LarielsQoLConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BetterBreedingListener {

    private final LarielsQoLConfig config;

    public BetterBreedingListener() {
        this.config = LarielsQoL.getConfig();
    }

    @SubscribeEvent
    public void onPreEggCalculate(DayCareEvent.PreEggCalculate event) {

        if (!config.getAllowUndiscoveredBreeding())
            return;

        if (!hasUnlockedUndiscovered(event.getPlayer()))
            return;

        var p1 = event.getParentOne();
        var p2 = event.getParentTwo();

        if (p1 == null || p2 == null)
            return;

        if (!areParentsBreedable(p1, p2))
            return;

        if (isBreedableEggGroup(p1) && isBreedableEggGroup(p2))
            return;

        if (!event.isChildExists() || event.getCalculatedChild() == null) {

            var species = p1.getSpecies().getName().equals("Ditto")
                    ? p2.getSpecies()
                    : p1.getSpecies();

            var child = PokemonFactory.create(species);

            event.setCalculatedChild(child);
            event.setChildExists(true);
            event.setShowChild(true);
            event.setShowPotentialShiny(true);
        }
    }

    @SubscribeEvent
    public void onPreEggCollect(DayCareEvent.PreCollect event) {

        var parent1 = event.getParentOne();
        var parent2 = event.getParentTwo();
        var egg = event.getChildGiven();

        if (parent1 == null || parent2 == null || egg == null) {
            return;
        }

        applyIvInheritance(egg, parent1, parent2);
        applyShinyLogic(egg, parent1, parent2);
        applyBaseFormLogic(egg, parent1, parent2);
    }

    @SubscribeEvent
    public void onPostCollect(DayCareEvent.PostCollect event) {

        var player = event.getPlayer();
        var child = event.getChildGiven();

        BreedingProgress.incrementCount(player);

        if (isBaby(child)) {
            BreedingProgress.setBredBaby(player);
        }

        if (hasUnlockedUndiscovered(player)) {
            triggerUnlockAdvancement(player);
        }
    }

    private boolean isBaby(Pokemon p) {
        var stats = p.getForm();
        if (stats == null) return false;

        if (p.isLegendary() || p.isMythical() || p.isUltraBeast())
            return false;

        if (!stats.getPreEvolutions().isEmpty())
            return false;

        if (stats.getEvolutions().isEmpty())
            return false;

        var eggGroups = stats.getEggGroups();

        return eggGroups.stream()
                .anyMatch(g -> g.getKey().endsWith("undiscovered"));
    }

    private void triggerUnlockAdvancement(ServerPlayer player) {
        var manager = player.server.getAdvancements();
        var adv = manager.get(ResourceLocation.fromNamespaceAndPath("larielsqol", "unlock_undiscovered"));

        if (adv != null) {
            player.getAdvancements().award(adv, "trigger");
        }
    }


    private boolean hasUnlockedUndiscovered(ServerPlayer player) {
        return BreedingProgress.getCount(player) >= config.getUndiscoveredBreedingCount()
                && BreedingProgress.hasBredBaby(player);
    }

    // ------------------------------------------------------------
    //  IV INHERITANCE
    // ------------------------------------------------------------

    private void applyIvInheritance(Pokemon egg, Pokemon p1, Pokemon p2) {

        var chance = config.getIvInheritanceChance();

        for (var stat : BattleStatsType.values()) {

            if (Math.random() < chance) {
                var iv1 = p1.getIVs().getStat(stat);
                var iv2 = p2.getIVs().getStat(stat);
                egg.getIVs().setStat(stat, Math.max(iv1, iv2));
            }
        }
    }

    // ------------------------------------------------------------
    //  SHINY LOGIC
    // ------------------------------------------------------------

    private void applyShinyLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        var shinyChance = config.getShinyBaseChance();

        var shinyParents = 0;
        if (p1.isShiny()) shinyParents++;
        if (p2.isShiny()) shinyParents++;

        for (var i = 0; i < shinyParents; i++) {
            shinyChance *= config.getShinyParentMultiplier();
        }

        if (Math.random() < shinyChance) {
            egg.setShiny(true);
        }
    }

    // ------------------------------------------------------------
    //  FORM / PALETTE LOGIC
    // ------------------------------------------------------------

    private void applyBaseFormLogic(Pokemon egg, Pokemon p1, Pokemon p2) {

        if (Math.random() >= config.getFormInheritanceChance()) {
            return;
        }

        var palette1 = safePaletteName(p1);
        var palette2 = safePaletteName(p2);

        var eggSupported = getSupportedPalettes(egg);

        var supportsP1 = eggSupported.contains(palette1);
        var supportsP2 = eggSupported.contains(palette2);

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

    private boolean isBreedableEggGroup(Pokemon p) {
        var stats = p.getForm();
        if (stats == null) return true;

        var eggGroups = stats.getEggGroups();
        if (eggGroups == null) return true;

        return eggGroups.stream()
                .noneMatch(g -> g.getKey().endsWith("undiscovered"));
    }

    private boolean areParentsBreedable(Pokemon p1, Pokemon p2) {

        var s1 = p1.getSpecies();
        var s2 = p2.getSpecies();

        var dittoRule =
                s1.getName().equals("Ditto") ||
                        s2.getName().equals("Ditto");

        var sameSpecies = s1.getDex() == s2.getDex();

        return dittoRule || sameSpecies;
    }
}