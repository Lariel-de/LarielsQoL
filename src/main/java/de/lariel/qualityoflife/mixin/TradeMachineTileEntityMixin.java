package de.lariel.qualityoflife.mixin;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.events.PokemonReceivedEvent;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.NetworkHelper;
import com.pixelmonmod.pixelmon.blocks.tileentity.TradeMachineTileEntity;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreenPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.RegisterTraderPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.SetSelectedStatsPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.SetTradeTargetPacket;
import com.pixelmonmod.pixelmon.entities.npcs.NPC;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("unused")
@Mixin(TradeMachineTileEntity.class)
public abstract class TradeMachineTileEntityMixin {
    @Shadow
    public ServerPlayer player1;
    @Shadow
    public ServerPlayer player2;
    @Shadow
    public boolean ready1;
    @Shadow
    public boolean ready2;
    @Shadow
    public int pos1;
    @Shadow
    public int pos2;
    @Shadow
    public String user1;
    @Shadow
    public String user2;
    @Shadow
    public int playerCount;
    @Shadow
    public CompoundTag poke1;
    @Shadow
    public CompoundTag poke2;
    @Shadow
    public boolean tradePushed;

    @Shadow
    public abstract void sendChanges();

    @Inject(method = "registerPlayer", at = @At("HEAD"), cancellable = true)
    private void larielsrebalancing$registerPlayer(ServerPlayer player, CallbackInfo ci) {
        if (player == null || !this.larielsrebalancing$hasNpcNearby()) {
            return;
        }

        if (player1 == null) {
            player1 = player;
            player2 = player;
            user1 = player.getName().getString();
            user2 = user1;
            ready1 = false;
            ready2 = false;
            pos1 = 0;
            pos2 = 0;
            playerCount = 2;
        }

        @SuppressWarnings("DataFlowIssue") var pos = ((net.minecraft.world.level.block.entity.BlockEntity) (Object) this).getBlockPos();
        OpenScreenPacket.open(player, EnumGuiScreen.Trading, pos.getX(), pos.getY(), pos.getZ());
        if (player == this.player2) {
            NetworkHelper.sendPacket(new RegisterTraderPacket(this.player1.getUUID(), this.hasMoreThan1HatchedPokemon(this.player1.getUUID())), this.player2);
            NetworkHelper.sendPacket(new RegisterTraderPacket(this.player2.getUUID(), this.hasMoreThan1HatchedPokemon(this.player2.getUUID())), this.player1);
            if (this.pos1 != -1) {
                var party = StorageProxy.getPartyNow(this.player1);
                if (party == null) return;

                var partyPos1 = party.get(this.pos1);
                if (partyPos1 == null) return;

                NetworkHelper.sendPacket(new SetTradeTargetPacket(party.get(this.pos1), new PixelmonStatsData(partyPos1.getStats())), this.player2);
                NetworkHelper.sendPacket(new SetSelectedStatsPacket(new PixelmonStatsData(partyPos1.getStats())), this.player1);
            }
        }

        sendChanges();
        ci.cancel();
    }

    @Inject(method = "trade", at = @At("HEAD"), cancellable = true)
    public void trade(CallbackInfo ci) {
        var party1 = StorageProxy.getPartyNow(this.player1);
        var party2 = StorageProxy.getPartyNow(this.player2);

        if (party1 == null || party2 == null) return;

        party1.retrieveAll("BlockInteract");
        party2.retrieveAll("BlockInteract");

        this.tradePushed = true;
        var pokemon1 = party1.get(this.pos1);
        var pokemon2 = party2.get(this.pos2);
        if (pokemon1 != null && pokemon2 != null) {
            if (this.hasOtherHatchedPokemon(this.pos1, party1, pokemon2.isEgg()) && this.hasOtherHatchedPokemon(this.pos2, party2, pokemon1.isEgg())) {
                if (Pixelmon.EVENT_BUS.post(new PixelmonTradeEvent.Pre(this.player1, this.player2, pokemon1, pokemon2)).isCanceled()) {
                    this.abortTrade();
                } else if (!Pixelmon.EVENT_BUS.post(new PokemonReceivedEvent(this.player1, pokemon2, "Trade")).isCanceled()) {
                    if (!Pixelmon.EVENT_BUS.post(new PokemonReceivedEvent(this.player2, pokemon1, "Trade")).isCanceled()) {
                        party1.transfer(party2, new StoragePosition(-1, this.pos2), new StoragePosition(-1, this.pos1));
                        this.player1.closeContainer();
                        this.player2.closeContainer();
                        this.playerCount = 0;
                        var pixelmon1 = pokemon1.getOrSpawnPixelmon(this.player1);
                        var pixelmon2 = pokemon2.getOrSpawnPixelmon(this.player2);

                        if (pixelmon1 == null || pixelmon2 == null) return;

                        var species1 = pokemon1.getSpecies();
                        var species2 = pokemon2.getSpecies();
                        pokemon1.setFriendship(pokemon1.getForm().getSpawn().getBaseFriendship());
                        if (!pixelmon1.testTradeEvolution(species2)) {
                            pixelmon1.retrieve("Forced");
                        }

                        pokemon2.setFriendship(pokemon2.getForm().getSpawn().getBaseFriendship());
                        if (!pixelmon2.testTradeEvolution(species1)) {
                            pixelmon2.retrieve("Forced");
                        }

                        this.sendChanges();
                        Pixelmon.EVENT_BUS.post(new PixelmonTradeEvent.Post(this.player1, this.player2, pokemon1, pokemon2));
                    }
                }
            } else {
                this.abortTrade();
            }
        } else {
            this.abortTrade();
        }

        ci.cancel();
    }

    @Unique
    private boolean hasMoreThan1HatchedPokemon(UUID player) {
        if (player == null) {
            return false;
        } else {
            var party1 = StorageProxy.getPartyNow(player);
            return this.hasMoreThan1HatchedPokemon(party1);
        }
    }

    @Unique
    private boolean hasMoreThan1HatchedPokemon(PlayerPartyStorage party) {
        var nonEggs = 0;

        for (var i = 0; i < 6; ++i) {
            var p = party.get(i);
            if (p != null && !p.isEgg()) {
                ++nonEggs;
            }

            if (nonEggs > 1) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private boolean hasOtherHatchedPokemon(int except, PlayerPartyStorage party, boolean isOtherAnEgg) {
        for (var i = 0; i < 6; ++i) {
            if (i != except) {
                var p = party.get(i);
                if (p != null && !p.isEgg()) {
                    return true;
                }
            }
        }

        return !isOtherAnEgg;
    }

    @Unique
    private void abortTrade() {
        if (this.player1 != null) {
            this.player1.closeContainer();
        }

        if (this.player2 != null) {
            this.player2.closeContainer();
        }

        this.playerCount = 0;
        this.sendChanges();
    }

    @Unique
    private boolean larielsrebalancing$hasNpcNearby() {
        @SuppressWarnings("DataFlowIssue") var self = (TradeMachineTileEntity) (Object) this;
        var level = self.getLevel();
        if (level == null || level.isClientSide()) {
            return false;
        }

        var pos = self.getBlockPos();
        var area = new AABB(pos).inflate(2.0D);
        return !level.getEntitiesOfClass(Entity.class, area, entity -> entity instanceof NPC).isEmpty();
    }
}