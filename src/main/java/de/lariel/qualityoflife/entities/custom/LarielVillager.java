package de.lariel.qualityoflife.entities.custom;

import com.mojang.serialization.Dynamic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LarielVillager extends Villager {

    public LarielVillager(EntityType<? extends Villager> type, Level level) {
        super(type, level);
        this.setVillagerData(new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
        this.setPersistenceRequired();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return super.makeBrain(dynamic); // Vanilla Brain
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();

        updateTrades();

        var offers = this.getOffers();
        if (offers.isEmpty()) return;

        var blocked = offers.stream().anyMatch(MerchantOffer::needsRestock);
        var partiallyUsed = offers.stream().anyMatch(o -> o.getUses() > 0 && !o.needsRestock());

        var level = this.getVillagerData().getLevel();

        var levelColor = switch (level) {
            case 1 -> ChatFormatting.GRAY;
            case 2 -> ChatFormatting.GREEN;
            case 3 -> ChatFormatting.BLUE;
            case 4 -> ChatFormatting.DARK_PURPLE;
            case 5 -> ChatFormatting.GOLD;
            default -> ChatFormatting.WHITE;
        };

        var profession = this.getVillagerData().getProfession();
        var professionKey = profession.toString();
        var professionName = I18n.get("entity.larielsqualityoflife.lariel_villager." + professionKey);

        var icon = Component.literal("●").withStyle(
                blocked ? ChatFormatting.RED :
                        partiallyUsed ? ChatFormatting.YELLOW :
                                ChatFormatting.GREEN
        );

        Component name = icon.append(
                Component.literal(" " + professionName).withStyle(levelColor)
        );

        this.setCustomName(name);
    }
}