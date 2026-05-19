package de.lariel.qualityoflife.network.packet;

import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.betterBreeding.LarielCostService;
import de.lariel.qualityoflife.network.packet.base.LarielPacketBase;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.utility.LarielCostEntry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LarielBetterBreedingApplyPacket extends LarielPacketBase {
    private final UUID eggUUID;
    private final String form;
    private final String palette;
    private final String gender;

    public LarielBetterBreedingApplyPacket(UUID eggUUID, String form, String palette, String gender) {
        super(true);

        this.eggUUID = eggUUID;
        this.form = form;
        this.palette = palette;
        this.gender = gender;
    }

    @Override
    protected void handlePacket(IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player))
            return;

        context.enqueueWork(() -> {
            var storage = StorageProxy.getPartyNow(player.getUUID());
            if (storage == null) return;

            var egg = storage.getTeam(p -> p.getUUID().equals(eggUUID)).getFirst();

            if (egg == null) return;

            List<LarielCostEntry> costs = new ArrayList<>();

            if (!egg.getForm().getName().equals(form)) {
                costs.add(LarielCostService.CalculateFormCosts(egg, form));
            }

            if (!egg.getPalette().getName().equals(palette)) {
                costs.add(LarielCostService.CalculatePaletteCosts(egg, palette));
            }

            if (!egg.getGender().name().equals(gender)) {
                costs.add(LarielCostService.CalculateGenderCosts(egg, gender));
            }

            var hasAll = costs.stream().allMatch(cost ->
                    player.getInventory().countItem(cost.item()) >= cost.amount()
            );

            if (!hasAll) {
                LarielNetwork.sendToClient(player, new LarielBetterBreedingStatusPacket("betterbreeding.larielsqualityoflife.notenoughitems.", false));
                return;
            }

            for (var cost : costs) {
                var remaining = cost.amount();

                for (var i = 0; i < player.getInventory().getContainerSize(); i++) {
                    var stack = player.getInventory().getItem(i);

                    if (stack.is(cost.item())) {
                        var take = Math.min(stack.getCount(), remaining);
                        stack.shrink(take);
                        remaining -= take;

                        if (remaining <= 0) break;
                    }
                }
            }

            egg.setForm(form);
            egg.setPalette(palette);
            egg.setGender(Gender.valueOf(gender));

            storage.set(egg.getPosition(), egg);

            LarielNetwork.sendToClient(player, new LarielBetterBreedingStatusPacket("betterbreeding.larielsqualityoflife.applied", true));
        });
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LarielBetterBreedingApplyPacket> CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, p -> p.eggUUID,
                    ByteBufCodecs.STRING_UTF8, p -> p.form,
                    ByteBufCodecs.STRING_UTF8, p -> p.palette,
                    ByteBufCodecs.STRING_UTF8, p -> p.gender,
                    LarielBetterBreedingApplyPacket::new
            );

    public static final Type<LarielBetterBreedingApplyPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "better_breeding_packet"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

