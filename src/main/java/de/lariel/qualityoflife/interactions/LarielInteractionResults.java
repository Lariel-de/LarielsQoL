package de.lariel.qualityoflife.interactions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pixelmonmod.pixelmon.api.context.ContextKeys;
import com.pixelmonmod.pixelmon.api.context.StoredContext;
import com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResult;
import com.pixelmonmod.pixelmon.api.npc.interaction.result.InteractionResultType;
import com.pixelmonmod.pixelmon.init.registry.PixelmonRegistry;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.menu.MintTraderMenuProvider;
import de.lariel.qualityoflife.network.packet.LarielBetterBreedingOpenScreenPacket;
import de.lariel.qualityoflife.network.packet.LarielShopkeeperOpenScreenPacket;
import de.lariel.qualityoflife.network.packet.LarielShopPurchaseSyncPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.listener.ShopkeeperReloadListener;
import de.lariel.qualityoflife.reputation.LarielPlayerReputationStoreManager;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopkeeperSerializer;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopPurchaseStore;
import de.lariel.qualityoflife.shopkeeper.utility.LarielShopkeeperStateManager;
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
    public static final InteractionResultType<OpenLarielShopkeeperInteractionResult> OPEN_LARIEL_SHOPKEEPER =
            InteractionResultType.of(OpenLarielShopkeeperInteractionResult.CODEC,
                    () -> new OpenLarielShopkeeperInteractionResult(""));

    private LarielInteractionResults() {
    }

    public static void register(RegisterEvent event) {
        event.register(PixelmonRegistry.INTERACTION_RESULT_TYPE_REGISTRY,
                helper -> {
                    helper.register(OPEN_BETTER_BREEDING_ID, OPEN_BETTER_BREEDING);
                    helper.register(ResourceLocation.fromNamespaceAndPath(
                            LarielsQoL.MOD_ID, "open_mint_trader"), OPEN_MINT_TRADER);
                    helper.register(ResourceLocation.fromNamespaceAndPath(
                            LarielsQoL.MOD_ID, "open_lariel_shopkeeper"), OPEN_LARIEL_SHOPKEEPER);
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

    public static final class OpenLarielShopkeeperInteractionResult
            implements InteractionResult {
        public static final MapCodec<OpenLarielShopkeeperInteractionResult> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Codec.STRING.fieldOf("shopkeeper_id")
                                .forGetter(result -> result.shopkeeperId)
                ).apply(instance, OpenLarielShopkeeperInteractionResult::new));

        private final String shopkeeperId;

        private OpenLarielShopkeeperInteractionResult(String shopkeeperId) {
            this.shopkeeperId = shopkeeperId;
        }

        @Override
        public MapCodec<OpenLarielShopkeeperInteractionResult> codec() {
            return CODEC;
        }

        @Override
        public InteractionResultType<?> type() {
            return OPEN_LARIEL_SHOPKEEPER;
        }

        @Override
        public void handle(StoredContext context) {
            context.getContext(ContextKeys.PLAYER).ifPresent(player -> {
                var id = ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, shopkeeperId);
                var definition = ShopkeeperReloadListener.SHOPKEEPERS.get(id);

                if (definition == null) {
                    LarielsQoL.LOGGER.warn("Unknown shopkeeper interaction id: {}", id);
                    return;
                }

                var reputationLevel = LarielPlayerReputationStoreManager.get(player).getLevel(id);
                var state = LarielShopkeeperStateManager.getState(id);
                var shopItems = state.getItemsForToday(player.serverLevel(), definition, reputationLevel);
                var shopItemsJson = LarielShopkeeperSerializer.serialize(shopItems, player.serverLevel().registryAccess());

                LarielNetwork.sendToClient(player,
                        new LarielShopkeeperOpenScreenPacket(id, shopItemsJson));
                for (var item : shopItems) {
                    LarielNetwork.sendToClient(player, new LarielShopPurchaseSyncPacket(
                            id, item.getShopItem().uuid(),
                            LarielShopPurchaseStore.getPurchasedToday(player, id, item)));
                }
            });
        }
    }
}
