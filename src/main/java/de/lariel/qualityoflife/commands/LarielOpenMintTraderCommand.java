package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.lariel.qualityoflife.menu.MintTraderMenuProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class LarielOpenMintTraderCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("openminttrader")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();

                    if (player == null)
                        return 0;
                    player.openMenu(new MintTraderMenuProvider());
                    return 1;
                })
        );
    }
}

