package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.lariel.qualityoflife.menu.MintTraderMenuProvider;
import de.lariel.qualityoflife.network.packet.LarielBetterBreedingOpenScreenPacket;
import de.lariel.qualityoflife.network.server.LarielNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class LarielOpenBetterBreedingCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("breedui")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();

                    LarielNetwork.sendToClient(player, new LarielBetterBreedingOpenScreenPacket(true));

                    return 1;
                })
        );
    }
}
