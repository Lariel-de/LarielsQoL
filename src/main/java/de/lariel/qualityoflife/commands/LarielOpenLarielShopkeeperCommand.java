package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.data.ShopkeeperDefinition;
import de.lariel.qualityoflife.data.ShopkeeperReloadListener;
import de.lariel.qualityoflife.network.packet.LarielShopkeeperOpenScreenPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import de.lariel.qualityoflife.shopkeeper.LarielShopItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class LarielOpenLarielShopkeeperCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("_zlariel:qol/internal/shopkeeperui")
                .then(Commands.argument("shopkeeperId", StringArgumentType.string())
                        .executes(ctx -> {
                            var shopkeeperId = StringArgumentType.getString(ctx, "shopkeeperId");
                            var player = ctx.getSource().getPlayer();

                            var id = ResourceLocation.fromNamespaceAndPath(LarielsQoL.MOD_ID, shopkeeperId);
                            var def = ShopkeeperReloadListener.SHOPKEEPERS.get(id);
                            var shopItems = convertToLarielShopItems(def);
                            var dtoList = shopItems.stream()
                                    .map(LarielShopItem::toJsonDto)
                                    .toList();

                            var json = LarielsQoL.GSON.toJson(dtoList);


                            LarielNetwork.sendToClient(player, new LarielShopkeeperOpenScreenPacket(json));

                            return 1;
                        })
                ));
    }

    private static List<LarielShopItem> convertToLarielShopItems(ShopkeeperDefinition def) {
        List<LarielShopItem> list = new ArrayList<>();

        for (var entry : def.levels.entrySet()) {
            var level = Integer.parseInt(entry.getKey());

            for (var trade : entry.getValue()) {
                list.add(LarielShopItem.fromJson(trade, level));
            }
        }

        return list;
    }
}