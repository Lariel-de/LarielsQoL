package de.lariel.qualityoflife.capture;

import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import de.lariel.qualityoflife.network.packet.LarielIWantStatusPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.*;

public final class LarielIWantService {
    private static final LarielIWantService INSTANCE = new LarielIWantService();

    private final Map<BattleController, BattleState> battles = new IdentityHashMap<>();

    private LarielIWantService() {
    }

    public static LarielIWantService getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void onBattleStarted(BattleStartedEvent.Post event) {
        var state = new BattleState(
                event.getTeamOne(),
                event.getTeamTwo()
        );
        battles.put(event.getBattleController(), state);

        sendState(state);
    }

    @SubscribeEvent
    public void onBattleEnded(BattleEndEvent event) {
        var state = battles.remove(event.getBattleController());
        if (state == null) {
            return;
        }

        for (var player : event.getPlayers()) {
            if (player instanceof ServerPlayer serverPlayer) {
                LarielNetwork.sendToClient(serverPlayer, LarielIWantStatusPacket.empty());
            }
        }
    }

    @SubscribeEvent
    public void onDamage(AttackEvent.Damage event) {
        var state = battles.get(event.getBattleController());
        if (state == null || state.protectedPokemon == null
                || !state.protectedPokemon.equals(event.target.getPokemonUUID())) {
            return;
        }

        event.damage = Math.max(0.0D, event.target.getHealth() - 1.0D);
    }

    public void toggle(ServerPlayer player, UUID targetUuid) {
        var state = findState(player);
        if (state == null || !state.wildTargets.contains(targetUuid)) {
            return;
        }

        state.protectedPokemon = targetUuid.equals(state.protectedPokemon) ? null : targetUuid;
        sendState(state);
    }

    private BattleState findState(ServerPlayer player) {
        for (var state : battles.values()) {
            if (state.players.contains(player)) {
                return state;
            }
        }
        return null;
    }

    private void sendState(BattleState state) {
        for (var player : state.players) {
            LarielNetwork.sendToClient(player, new LarielIWantStatusPacket(
                    state.wildTargets,
                    state.protectedPokemon
            ));
        }
    }

    private static final class BattleState {
        private final List<UUID> wildTargets = new ArrayList<>();
        private final List<ServerPlayer> players = new ArrayList<>();
        private UUID protectedPokemon;

        private BattleState(BattleParticipant[] teamOne, BattleParticipant[] teamTwo) {
            for (var participant : teamOne) {
                addPlayer(participant);
                addWildTargetsForPlayer(participant, teamTwo);
            }
            for (var participant : teamTwo) {
                addPlayer(participant);
                addWildTargetsForPlayer(participant, teamOne);
            }
        }

        private void addPlayer(BattleParticipant participant) {
            if (participant.getEntity() instanceof ServerPlayer player && !players.contains(player)) {
                players.add(player);
            }
        }

        private void addWildTargetsForPlayer(BattleParticipant participant, BattleParticipant[] opposingTeam) {
            if (!participant.isPlayer()) {
                return;
            }

            for (var opponent : opposingTeam) {
                if (opponent.isPlayer()) {
                    continue;
                }

                for (var pokemon : opponent.controlledPokemon) {
                    if (pokemon.isWildPokemon() && !wildTargets.contains(pokemon.getPokemonUUID())) {
                        wildTargets.add(pokemon.getPokemonUUID());
                    }
                }
            }
        }
    }
}
