package de.lariel.qualityoflife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.betterBreeding.BreedingProgress;
import de.lariel.qualityoflife.listener.LarielEntityTrackListener;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.UnknownNullability;

public class LarielReloadConfigCommand {
    public static void register(@UnknownNullability CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("larielsqolreload")
                .requires(src -> src.hasPermission(2))
                .executes(ctx -> {
                    LarielsQoL.get_instance().reloadConfig();

                    ctx.getSource().sendSuccess(
                            () -> Component.translatable("LarielsQoL config reloaded."),
                            true
                    );
                    return 1;
                }));
    }
}
