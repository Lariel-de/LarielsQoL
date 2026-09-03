package de.lariel.qualityoflife.interactions;

import com.mojang.serialization.MapCodec;
import com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResultType;
import com.pixelmonmod.pixelmon.init.registry.PixelmonRegistry;
import de.lariel.qualityoflife.LarielsQoL;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class LarielInteractionResults {
    public static final ResourceLocation OPEN_BETTER_BREEDING_ID =
            ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, "open_better_breeding");

    public static final InteractionResultType<OpenBetterBreedingInteractionResult> OPEN_BETTER_BREEDING =
            InteractionResultType.of(OpenBetterBreedingInteractionResult.CODEC,
                    OpenBetterBreedingInteractionResult::new);

    private LarielInteractionResults() {
    }

    public static void register(RegisterEvent event) {
        event.register(PixelmonRegistry.INTERACTION_RESULT_TYPE_REGISTRY,
                helper -> helper.register(OPEN_BETTER_BREEDING_ID, OPEN_BETTER_BREEDING));
    }

    public static final class OpenBetterBreedingInteractionResult
            implements com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResult {
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
        public void handle(com.pixelmonmod.pixelmon.api.context.StoredContext context) {
            context.getContext(com.pixelmonmod.pixelmon.api.context.ContextKeys.PLAYER)
                    .ifPresent(player -> de.lariel.qualityoflife.network.server.LarielNetwork.sendToClient(
                            player,
                            new de.lariel.qualityoflife.network.packet.LarielBetterBreedingOpenScreenPacket(true)));
        }
    }
}
