package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.lariel.qualityoflife.listener.LarielEntityTrackListener;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.UnknownNullability;

public class LarielTrackEntityCommand {
    public static void register(@UnknownNullability CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trackspawn")
                .then(Commands.argument("entityId", IntegerArgumentType.integer())
                        .executes(ctx ->
                        {
                            var player = ctx.getSource().getPlayerOrException();
                            var id = IntegerArgumentType.getInteger(ctx, "entityId");

                            var target = player.level().getEntity(id);
                            if (target == null) {
                                ctx.getSource().sendFailure(Component.translatable("spawnnotification.larielsqualityoflife.entity_not_found"));
                                return 0;
                            }

                            LarielEntityTrackListener.GetInstance().startTracking(player, target);

                            ctx.getSource().sendSuccess(
                                    () -> Component.translatable("spawnnotification.larielsqualityoflife.start_tracking"),
                                    false
                            );

                            return 1;
                        })));
    }
}
