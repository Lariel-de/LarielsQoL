package de.lariel.qualityoflife.utility;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Locale;

public class LarielSpawnNotifier {

    public void NotifyBoss(PixelmonEntity entity)
    {
        var component = Component.translatable("spawnnotification.larielsqualityoflife.boss.prefix")
                .append(GetLocalizedPokemonName(entity.getPokemon()))
                .append(Component.translatable("spawnnotification.larielsqualityoflife.boss.postfix"));

        NotifyPlayers(entity, component);
    }

    public void NotifyShiny(PixelmonEntity entity)
    {
        var component = Component.translatable("spawnnotification.larielsqualityoflife.shiny.prefix")
                .append(GetLocalizedPokemonName(entity.getPokemon()))
                .append(Component.translatable("spawnnotification.larielsqualityoflife.shiny.postfix"));

        NotifyPlayers(entity, component);
    }

    public void NotifyLegendary(PixelmonEntity entity)
    {
        var component = Component.translatable("spawnnotification.larielsqualityoflife.legendary.prefix")
                .append(GetLocalizedPokemonName(entity.getPokemon()))
                .append(Component.translatable("spawnnotification.larielsqualityoflife.legendary.postfix"));

        NotifyPlayers(entity, component);
    }

    public void NotifyUltraBeast(PixelmonEntity entity)
    {
        var component = Component.translatable("spawnnotification.larielsqualityoflife.ultrabeast.prefix")
                .append(GetLocalizedPokemonName(entity.getPokemon()))
                .append(Component.translatable("spawnnotification.larielsqualityoflife.ultrabeast.postfix"));

        NotifyPlayers(entity, component);
    }

    public void NotifySpecialPalette(PixelmonEntity entity)
    {
        var component = Component.translatable("spawnnotification.larielsqualityoflife.specialpalette.prefix")
                .append(GetLocalizedPokemonName(entity.getPokemon()))
                .append(" - " + entity.getPokemon().getPaletteName())
                .append(Component.translatable("spawnnotification.larielsqualityoflife.specialpalette.postfix"));

        NotifyPlayers(entity, component);
    }

    private void NotifyPlayers(PixelmonEntity entity, MutableComponent component) {
        var server = ServerLifecycleHooks.getCurrentServer();

        if (server == null)
            return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(component);

            var coords = GetCoords(entity);
            var coordsComponent = Component.literal("[" + coords.x() + " " + coords.y() + " " + coords.z() + "]")
                    .withStyle(style -> style.withColor(TextColor.fromRgb(0x55FF55))
                            .withUnderlined(true) .withClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tp @s " + coords.x() + " " + coords.y() + " " + coords.z() ))
                            .withHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, Component.literal("Teleport to these coordinates") )) );
            var atMessage = Component.translatable("spawnnotification.larielsqualityoflife.at").append(coordsComponent);

            player.sendSystemMessage(atMessage);
        }
    }

    private LarielCoordinates GetCoords(PixelmonEntity entity) {
        return new LarielCoordinates(entity.getX(), entity.getY(), entity.getZ());
    }

    private MutableComponent GetLocalizedPokemonName(Pokemon pokemon) {
        var pokemonSpecies = pokemon.getSpecies();
        var speciesName = pokemonSpecies.getName();
        var translationKey = "pixelmon." + speciesName.toLowerCase(Locale.ROOT);

        return Component.translatable(translationKey)
                .withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)
                        .withBold(true));
    }
}
