package de.lariel.qualityoflife.listener;

import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.neoforged.bus.api.SubscribeEvent;

public class LarielPokeSpawnListener {

    private final LarielSpawnNotifier _notifier;

    public LarielPokeSpawnListener(LarielSpawnNotifier notifier) {
        _notifier = notifier;
    }

    @SubscribeEvent
    public void onEntitySpawn(SpawnEvent event) {
        var action = event.action;

        if (!(action instanceof SpawnActionPokemon pokemonAction)) {
            return;
        }

        var pixelmonEntity = pokemonAction.getOrCreateEntity();
        var pokemon = pokemonAction.pokemon;

        if (!IsValidPokemon(pixelmonEntity, pokemon)) {
            return;
        }

        if (pixelmonEntity.isBossPokemon()) {
            _notifier.NotifyBoss(pixelmonEntity);
            return;
        }

        if (pokemon.isShiny()) {
            _notifier.NotifyShiny(pixelmonEntity);
            return;
        }

        if (pokemon.isLegendary()) {
            _notifier.NotifyLegendary(pixelmonEntity);
            return;
        }

        if (pokemon.isUltraBeast()) {
            _notifier.NotifyUltraBeast(pixelmonEntity);
            return;
        }

        if (!pokemon.isDefaultPalette()) {
            _notifier.NotifySpecialPalette(pixelmonEntity);
        }
    }

    private boolean IsValidPokemon(PixelmonEntity entity, Pokemon pokemon) {
        if (pokemon == null && entity == null) {
            return false;
        }

        var isWildPokemon = entity.getOwnerUUID() == null;
        var isInBattle = entity.getPokemon().getBattleController() != null;

        return isWildPokemon && !isInBattle && entity.isAlive();
    }
}
