package de.lariel.qualityoflife.businessLogic;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;

public class LarielPreviewFactory {
    public static Pokemon createEggPreview(Pokemon egg) {
        Pokemon preview = PokemonBuilder.copy(egg).egg(false).build();
        preview.hatchEgg();

        return preview;
    }
}

