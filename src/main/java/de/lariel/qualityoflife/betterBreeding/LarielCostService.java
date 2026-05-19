package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.config.LarielsQolBetterBreedingConfig;
import de.lariel.qualityoflife.utility.LarielCostEntry;
import net.minecraft.world.item.Item;

public class LarielCostService {
    private static final LarielsQolBetterBreedingConfig config;

    static {
        config = LarielsQoL.getConfig().breeding();
    }

    public static LarielCostEntry CalculateFormCosts(Pokemon egg, String form) {
        if (!egg.getForm().getName().equals(form)) {
            return resolveCost(
                    config.getFormGuaranteeCostItem(),
                    config.getFormGuaranteeCostAmount()
            );
        }

        return null;
    }

    public static LarielCostEntry CalculatePaletteCosts(Pokemon egg, String palette) {
        if (!egg.getPalette().getName().equals(palette)) {
            if (palette.equalsIgnoreCase("shiny"))
                return resolveCost(
                        config.getShinyGuaranteeCostItem(),
                        config.getShinyGuaranteeCostAmount()
                );
            else
                return resolveCost(
                        config.getPaletteGuaranteeCostItem(),
                        config.getPaletteGuaranteeCostAmount()
                );
        }

        return null;
    }

    public static LarielCostEntry CalculateGenderCosts(Pokemon egg, String gender) {
        if (!egg.getGender().name().equals(gender)) {
            return resolveCost(
                    config.getGenderGuaranteeCostItem(),
                    config.getGenderGuaranteeCostAmount()
            );
        }

        return null;
    }

    private static LarielCostEntry resolveCost(Item item, int amount) {
        return new LarielCostEntry(item, amount);
    }
}
