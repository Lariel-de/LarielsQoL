package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.lariel.qualityoflife.betterBreeding.BreedingProgress;
import de.lariel.qualityoflife.listener.LarielEntityTrackListener;
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
                                    BreedingProgress.reset(player);
                                    ctx.getSource().sendSuccess(() -> Component.translatable("Breeding progress reset for " + player.getName().getString()), true);
                                    return 1;
                                }))));
    }
}
