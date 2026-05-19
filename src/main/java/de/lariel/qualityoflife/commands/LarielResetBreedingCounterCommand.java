package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.lariel.qualityoflife.betterBreeding.LarielBreedingProgress;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.UnknownNullability;

public class LarielResetBreedingCounterCommand {
    public static void register(@UnknownNullability CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("larielresetbreedingprogress")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                    LarielBreedingProgress.reset(player);
                                    ctx.getSource().sendSuccess(() -> Component.translatable("Breeding progress reset for " + player.getName().getString()), true);
                                    return 1;
                                }))));
    }
}
