package de.lariel.qualityoflife.interactions;

import com.mojang.serialization.MapCodec;
import com.pixelmonmod.pixelmon.api.context.ContextKeys;
import com.pixelmonmod.pixelmon.api.context.StoredContext;
import com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResult;
import com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResultType;
import com.pixelmonmod.pixelmon.init.registry.PixelmonRegistry;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.menu.MintTraderMenuProvider;
import de.lariel.qualityoflife.network.packet.LarielBetterBreedingOpenScreenPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class LarielInteractionResults {
    public static final ResourceLocation OPEN_BETTER_BREEDING_ID =
            ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "open_better_breeding");

    public static final InteractionResultType<OpenBetterBreedingInteractionResult> OPEN_BETTER_BREEDING =
            InteractionResultType.of(OpenBetterBreedingInteractionResult.CODEC,
                    OpenBetterBreedingInteractionResult::new);
    public static final InteractionResultType<OpenMintTraderInteractionResult> OPEN_MINT_TRADER =
            InteractionResultType.of(OpenMintTraderInteractionResult.CODEC,
                    OpenMintTraderInteractionResult::new);

    private LarielInteractionResults() {
    }

    public static void register(RegisterEvent event) {
        event.register(PixelmonRegistry.INTERACTION_RESULT_TYPE_REGISTRY,
                helper -> {
                    helper.register(OPEN_BETTER_BREEDING_ID, OPEN_BETTER_BREEDING);
                    helper.register(ResourceLocation.fromNamespaceAndPath(
                            LarielsQoL.MOD_ID, "open_mint_trader"), OPEN_MINT_TRADER);
                });
    }

    public static final class OpenBetterBreedingInteractionResult
            implements InteractionResult {
        public static final MapCodec<OpenBetterBreedingInteractionResult> CODEC =
                MapCodec.unit(OpenBetterBreedingInteractionResult::new);

        private OpenBetterBreedingInteractionResult() {
        }

        @Override
        public MapCodec<OpenBetterBreedingInteractionResult> codec() {
            return CODEC;
        }

        @Override
        public InteractionResultType<?> type() {
            return OPEN_BETTER_BREEDING;
        }

        @Override
        public void handle(StoredContext context) {
            context.getContext(ContextKeys.PLAYER)
                    .ifPresent(player -> LarielNetwork.sendToClient(
                            player,
                            new LarielBetterBreedingOpenScreenPacket(true)));
        }
    }

    public static final class OpenMintTraderInteractionResult
            implements InteractionResult {
        public static final MapCodec<OpenMintTraderInteractionResult> CODEC =
                MapCodec.unit(OpenMintTraderInteractionResult::new);

        private OpenMintTraderInteractionResult() {
        }

        @Override
        public MapCodec<OpenMintTraderInteractionResult> codec() {
            return CODEC;
        }

        @Override
        public InteractionResultType<?> type() {
            return OPEN_MINT_TRADER;
        }

        @Override
        public void handle(StoredContext context) {
            context.getContext(ContextKeys.PLAYER)
                    .ifPresent(player -> player.openMenu(new MintTraderMenuProvider()));
        }
    }
}
