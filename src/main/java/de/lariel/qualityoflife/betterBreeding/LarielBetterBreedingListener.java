package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielFormService;
import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielIvInheritanceService;
import de.lariel.qualityoflife.businessLogic.betterBreeding.LarielShinyService;
import de.lariel.qualityoflife.enchantments.ArmorBonusService;
import de.lariel.qualityoflife.utility.AdvancementService;
import net.neoforged.bus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class LarielBetterBreedingListener {

    private final LarielBreedingRules rules;
    private final LarielIvInheritanceService ivService;
    private final LarielShinyService shinyService;
    private final LarielFormService formService;
    private final AdvancementService advancementService;
    private final Logger logger;

    public LarielBetterBreedingListener() {
        var breedingConfig = LarielsQoL.getConfig().breeding();
        this.logger = LarielsQoL.getLogger();
        this.rules = new LarielBreedingRules(breedingConfig);
        this.ivService = new LarielIvInheritanceService(breedingConfig, logger);
        this.shinyService = new LarielShinyService(breedingConfig, logger);
        this.formService = new LarielFormService(breedingConfig, logger);
        this.advancementService = new AdvancementService();
    }

    @SubscribeEvent
    public void onPreEggCalculate(DayCareEvent.PreEggCalculate event) {

        if (!rules.canUseUndiscoveredBreeding(event.getPlayer()))
            return;

        var p1 = event.getParentOne();
        var p2 = event.getParentTwo();

        if (!rules.canParentsBreed(p1, p2))
            return;

        if (rules.isNormalBreedablePair(p1, p2))
            return;

        rules.forceUndiscoveredChild(event);
    }

    @SubscribeEvent
    public void onPreEggCollect(DayCareEvent.PreCollect event) {

        var p1 = event.getParentOne();
        var p2 = event.getParentTwo();
        var egg = event.getChildGiven();

        if (p1 == null || p2 == null || egg == null)
            return;

        ivService.applyIvInheritance(egg, p1, p2);
        shinyService.applyShinyLogic(egg, p1, p2);
        formService.applyFormInheritance(egg, p1, p2);
        ArmorBonusService.applyBreedingBonuses(event.getPlayer(), egg);
    }

    @SubscribeEvent
    public void onPostCollect(DayCareEvent.PostCollect event) {

        var player = event.getPlayer();
        var child = event.getChildGiven();

        LarielBreedingProgress.incrementCount(player);

        logger.info("Breeding count: {}", LarielBreedingProgress.getCount(player));

        if (rules.isBaby(child)) {
            LarielBreedingProgress.incrementBredBabyCount(player);
            logger.info("Baby breeding count: {}", LarielBreedingProgress.getBredBabyCount(player));
        }

        if (rules.hasUnlockedUndiscovered(player)) {
            advancementService.triggerUnlockUndiscoverBreeding(player);
        }
    }
}
