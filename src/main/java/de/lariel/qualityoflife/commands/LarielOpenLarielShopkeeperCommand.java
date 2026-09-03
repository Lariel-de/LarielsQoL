package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.listener.ShopkeeperReloadListener;
import de.lariel.qualityoflife.network.packet.LarielShopkeeperOpenScreenPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.utility.LarielShopkeeperSerializer;
import de.lariel.qualityoflife.utility.LarielShopkeeperStateManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;

public class LarielOpenLarielShopkeeperCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("_zlariel:qol/internal/shopkeeperui")
                .then(Commands.argument("shopkeeperId", StringArgumentType.string())
                        .executes(ctx -> {
                            var shopkeeperId = StringArgumentType.getString(ctx, "shopkeeperId");
                            var player = ctx.getSource().getPlayer();

                            var serverLevel = ctx.getSource().getLevel();

                            var id = ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, shopkeeperId);
                            var def = ShopkeeperReloadListener.SHOPKEEPERS.get(id);

                            var shopkeeperLevel = 1;

                            var state = LarielShopkeeperStateManager.getState(id);
                            var shopItems = state.getItemsForToday(serverLevel, def, shopkeeperLevel);

                            var json = LarielShopkeeperSerializer.serialize(shopItems);
                            LarielNetwork.sendToClient(player, new LarielShopkeeperOpenScreenPacket(json));

                            return 1;
                        })
                ));
    }
}