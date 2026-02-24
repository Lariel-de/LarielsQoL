package de.lariel.qualityoflife.mixin;

import com.pixelmonmod.pixelmon.init.registry.BlockRegistration;
import com.pixelmonmod.pixelmon.listener.ZygardeCellsListener;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZygardeCellsListener.class)
public abstract class LarielZygardeCellListenerMixin {
    @Inject(method = "spawnOn", at = @At("TAIL"))
    private static void onZygardeSpawn(ChunkAccess chunk, BlockPos pos, Direction facing, ServerPlayer player,
                                       CallbackInfo ci) {
        handleZygardeSpawn(chunk, pos, player);
    }

    private static void handleZygardeSpawn(ChunkAccess chunk, BlockPos pos, ServerPlayer player) {
        var worldPos = chunk.getPos().getWorldPosition().offset(pos);

        var level = chunk.getLevel();
        if (level == null) return;

        var state = level.getBlockState(worldPos);

        var isCore = state.getBlock() == BlockRegistration.ZYGARDE_CORE.value();
        var isCell = state.getBlock() == BlockRegistration.ZYGARDE_CELL.value();

        if (!isCore && !isCell) return;

        LarielSpawnNotifier.GetInstance().NotifyZygarde(player, pos, isCore);
    }
}
