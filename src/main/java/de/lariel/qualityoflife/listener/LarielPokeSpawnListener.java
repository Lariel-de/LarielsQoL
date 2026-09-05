package de.lariel.qualityoflife.listener;

import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnActionPokemon;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
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

        if (!LarielsQoL.getConfig().general().getEnableSpawnNotification()) {
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

    @SubscribeEvent
    public void AdjustSpawnLevel(SpawnEvent event) {
        if (!LarielsQoL.getConfig().general().getEnableSpawnLevelAdjustment()
                || !(event.action instanceof SpawnActionPokemon action)) {
            return;
        }

        var location = action.spawnLocation;
        if (location == null
                || !(location.cause instanceof ServerPlayer player)
                || location.location == null
                || !(location.location.world instanceof ServerLevel serverLevel)
                || serverLevel.dimension() != Level.OVERWORLD) {
            return;
        }

        var entity = action.getOrCreateEntity();
        var pokemon = action.pokemon;
        if (!IsValidPokemon(entity, pokemon)) {
            return;
        }

        if (!(action.spawnInfo instanceof SpawnInfoPokemon spawnInfo)) {
            return;
        }

        var party = StorageProxy.getPartyNow(player.getUUID());
        if (party == null) {
            return;
        }

        int maxLevel = Math.max(1, LarielsQoL.getConfig().general().getOverworldSpawnMaxLevel());
        int currentLevel = pokemon.getPokemonLevel();
        if (currentLevel >= maxLevel) {
            return;
        }

        int targetLevel = Math.min(maxLevel, Math.max(spawnInfo.minLevel, party.getAverageLevel()));
        if (targetLevel <= currentLevel) {
            return;
        }

        pokemon.setLevel(targetLevel);
        entity.setHealth(pokemon.getHealth());
    }

    private boolean IsValidPokemon(PixelmonEntity entity, Pokemon pokemon) {
        if (pokemon == null || entity == null) {
            return false;
        }

        var isWildPokemon = entity.getOwnerUUID() == null;
        var isInBattle = entity.getPokemon().getBattleController() != null;

        return isWildPokemon && !isInBattle && entity.isAlive();
    }
}
