package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.lariel.qualityoflife.listener.LarielBlockTrackListener;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.UnknownNullability;

public class LarielTrackBlockCommand {
    public static void register(@UnknownNullability CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trackblock")
                .then(Commands.argument("x", IntegerArgumentType.integer())
                        .then(Commands.argument("y", IntegerArgumentType.integer())
                                .then(Commands.argument("z", IntegerArgumentType.integer())
                                        .executes(ctx -> {

                                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                                            int x = IntegerArgumentType.getInteger(ctx, "x");
                                            int y = IntegerArgumentType.getInteger(ctx, "y");
                                            int z = IntegerArgumentType.getInteger(ctx, "z");

                                            BlockPos pos = new BlockPos(x, y, z);

                                            LarielBlockTrackListener.GetInstance().startTracking(player, pos);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.translatable("spawnnotification.larielsqualityoflife.start_tracking")
                                                            .append(pos.getX() + " ")
                                                            .append(pos.getY() + " ")
                                                            .append(pos.getZ() + " "),
                                                    false
                                            );

                                            return 1;
                                        }))))
        );

    }
}
