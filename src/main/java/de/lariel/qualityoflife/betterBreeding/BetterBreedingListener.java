package de.lariel.qualityoflife.betterBreeding;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import de.lariel.qualityoflife.LarielsQoL;
import de.lariel.qualityoflife.enchantments.ArmorBonusService;
import de.lariel.qualityoflife.utility.AdvancementService;
import net.neoforged.bus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class BetterBreedingListener {

    private final BreedingRules rules;
    private final IvInheritanceService ivService;
    private final ShinyService shinyService;
    private final FormService formService;
    private final AdvancementService advancementService;
    private final Logger logger;

    public BetterBreedingListener() {
        var breedingConfig = LarielsQoL.getConfig().breeding();
        this.logger = LarielsQoL.getLogger();
        this.rules = new BreedingRules(breedingConfig);
        this.ivService = new IvInheritanceService(breedingConfig, logger);
        this.shinyService = new ShinyService(breedingConfig, logger);
        this.formService = new FormService(breedingConfig, logger);
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

        BreedingProgress.incrementCount(player);

        logger.info("Breeding count: {}", BreedingProgress.getCount(player));

        if (rules.isBaby(child)) {
            BreedingProgress.incrementBredBabyCount(player);
            logger.info("Baby breeding count: {}", BreedingProgress.getBredBabyCount(player));
        }

        if (rules.hasUnlockedUndiscovered(player)) {
            advancementService.triggerUnlockUndiscoverBreeding(player);
        }
    }
}
