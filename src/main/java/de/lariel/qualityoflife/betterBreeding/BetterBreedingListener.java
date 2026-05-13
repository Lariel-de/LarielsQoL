package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.utility.AdvancementService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class BetterBreedingListener {

    private final BreedingRules rules;
    private final IvInheritanceService ivService;
    private final ShinyService shinyService;
    private final FormService formService;
    private final AdvancementService advancementService;

    public BetterBreedingListener() {
        var config = LarielsQoL.getConfig();
        this.rules = new BreedingRules(config);
        this.ivService = new IvInheritanceService(config);
        this.shinyService = new ShinyService(config);
        this.formService = new FormService(config);
        this.advancementService = new AdvancementService();
    }

    @SubscribeEvent
    public void onPreEggCalculate(DayCareEvent.PreEggCalculate event) {

        if (!rules.canUseUndiscoveredBreeding(event.getPlayer()))
            return;

        Pokemon p1 = event.getParentOne();
        Pokemon p2 = event.getParentTwo();

        if (!rules.canParentsBreed(p1, p2))
            return;

        if (rules.isNormalBreedablePair(p1, p2))
            return;

        rules.forceUndiscoveredChild(event);
    }

    @SubscribeEvent
    public void onPreEggCollect(DayCareEvent.PreCollect event) {

        Pokemon p1 = event.getParentOne();
        Pokemon p2 = event.getParentTwo();
        Pokemon egg = event.getChildGiven();

        if (p1 == null || p2 == null || egg == null)
            return;

        ivService.applyIvInheritance(egg, p1, p2);
        shinyService.applyShinyLogic(egg, p1, p2);
        formService.applyFormInheritance(egg, p1, p2);
    }

    @SubscribeEvent
    public void onPostCollect(DayCareEvent.PostCollect event) {

        ServerPlayer player = event.getPlayer();
        Pokemon child = event.getChildGiven();

        BreedingProgress.incrementCount(player);

        if (rules.isBaby(child)) {
            BreedingProgress.incrementBredBabyCount(player);
        }

        if (rules.hasUnlockedUndiscovered(player)) {
            advancementService.triggerUnlockUndiscoverBreeding(player);
        }
    }
}
