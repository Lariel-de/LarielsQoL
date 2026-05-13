package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import de.lariel.qualityoflife.config.LarielsQoLConfig;
import net.minecraft.server.level.ServerPlayer;

public class BreedingRules {

    private final LarielsQoLConfig config;

    public BreedingRules(LarielsQoLConfig config) {
        this.config = config;
    }

    public boolean canUseUndiscoveredBreeding(ServerPlayer player) {
        return config.getAllowUndiscoveredBreeding() && hasUnlockedUndiscovered(player);
    }

    public boolean hasUnlockedUndiscovered(ServerPlayer player) {
        return BreedingProgress.getCount(player) >= config.getUndiscoveredBreedingCount()
                && BreedingProgress.getBredBabyCount(player) >= config.getUndiscoveredBabyBreedingCount();
    }

    public boolean canParentsBreed(Pokemon p1, Pokemon p2) {
        if (p1 == null || p2 == null) return false;

        boolean dittoRule = p1.getSpecies().getName().equals("Ditto")
                || p2.getSpecies().getName().equals("Ditto");

        boolean sameSpecies = p1.getSpecies().getDex() == p2.getSpecies().getDex();

        return dittoRule || sameSpecies;
    }

    public boolean isNormalBreedablePair(Pokemon p1, Pokemon p2) {
        return isBreedableEggGroup(p1) && isBreedableEggGroup(p2);
    }

    public void forceUndiscoveredChild(DayCareEvent.PreEggCalculate event) {

        var p1 = event.getParentOne();
        var p2 = event.getParentTwo();

        var species = p1.getSpecies().getName().equals("Ditto")
                ? p2.getSpecies()
                : p1.getSpecies();

        var child = PokemonFactory.create(species);

        event.setCalculatedChild(child);
        event.setChildExists(true);
        event.setShowChild(true);
        event.setShowPotentialShiny(true);
    }

    public boolean isBreedableEggGroup(Pokemon p) {
        var stats = p.getForm();
        if (stats == null) return true;

        return stats.getEggGroups().stream()
                .noneMatch(g -> g.getKey().endsWith("undiscovered"));
    }

    public boolean isBaby(Pokemon p) {

        var stats = p.getForm();
        if (stats == null) return false;

        if (p.isLegendary() || p.isMythical() || p.isUltraBeast())
            return false;

        if (!stats.getPreEvolutions().isEmpty())
            return false;

        if (stats.getEvolutions().isEmpty())
            return false;

        return stats.getEggGroups().stream()
                .anyMatch(g -> g.getKey().endsWith("undiscovered"));
    }
}