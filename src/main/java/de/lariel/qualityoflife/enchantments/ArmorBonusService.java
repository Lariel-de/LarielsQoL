package de.lariel.qualityoflife.enchantments;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.config.LarielsQolEnchantmentConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ArmorBonusService {

    private static final LarielsQolEnchantmentConfig enchantmentConfig;

    static {
        enchantmentConfig = LarielsQoL.getConfig().enchantments();
    }

    public static void applyBreedingBonuses(ServerPlayer player, Pokemon egg) {

        if (!hasWarmSteps(player))
            return;

        if (egg == null)
            return;

        var oldCycles = egg.getEggCycles();
        var reduction = enchantmentConfig.getWarmStepsMultiplier();

        var factor = 1.0 - reduction;

        var newCycles = Math.max(1, (int) Math.ceil(oldCycles * factor));

        egg.setEggCycles(newCycles);
    }

    private static boolean hasWarmSteps(ServerPlayer player) {
        @SuppressWarnings("resource")
        var registry = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);

        var warmStepsHolder = registry
                .getHolder(ResourceLocation.fromNamespaceAndPath("larielsqol", "warm_steps"))
                .orElse(null);

        if (warmStepsHolder == null)
            return false;

        return player.getInventory().armor.stream()
                .anyMatch(stack -> stack.getEnchantmentLevel(warmStepsHolder) > 0);
    }
}
