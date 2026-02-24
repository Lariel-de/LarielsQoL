package de.lariel.qualityoflife.listener;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class LarielPokeSpawnListener {

    private final LarielSpawnNotifier _notifier;

    public LarielPokeSpawnListener(LarielSpawnNotifier notifier) {
        _notifier = notifier;
    }

    @SubscribeEvent
    public void Notify(EntityJoinLevelEvent event) {
        var entity = event.getEntity();

        if (event.getLevel().isClientSide() || !(entity instanceof PixelmonEntity pixelmonEntity)) {
            return;
        }

        var pokemon = pixelmonEntity.getPokemon();

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
