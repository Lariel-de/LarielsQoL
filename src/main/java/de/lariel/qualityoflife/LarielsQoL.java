package de.lariel.qualityoflife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import de.lariel.qualityoflife.betterBreeding.LarielBetterBreedingListener;
import de.lariel.qualityoflife.capture.LarielIWantService;
import de.lariel.qualityoflife.client.LarielHotkeyHandler;
import de.lariel.qualityoflife.commands.LarielResetBreedingCounterCommand;
import de.lariel.qualityoflife.commands.LarielTrackBlockCommand;
import de.lariel.qualityoflife.commands.LarielTrackEntityCommand;
import de.lariel.qualityoflife.config.LarielsQolConfigManager;
import de.lariel.qualityoflife.entities.LarielEntityRegistration;
import de.lariel.qualityoflife.interactions.LarielInteractionResults;
import de.lariel.qualityoflife.items.LarielCreateModeTabs;
import de.lariel.qualityoflife.items.LarielItemRegistration;
import de.lariel.qualityoflife.listener.*;
import de.lariel.qualityoflife.menu.registry.LarielsQolModMenus;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LarielsQoL.MOD_ID)
@EventBusSubscriber(modid = LarielsQoL.MOD_ID)
public class LarielsQoL {

    public static final String MOD_ID = "larielsqualityoflife";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Gson GSON = new GsonBuilder().create();
    private static LarielsQoL _instance;
    private LarielsQolConfigManager _configManager;

    public LarielsQoL(IEventBus bus) {
        _instance = this;

        reloadConfig();

        bus.addListener(LarielInteractionResults::register);
        LarielsQolModMenus.MENUS.register(bus);

        NeoForge.EVENT_BUS.register(new LarielHotkeyHandler());

        bus.addListener(LarielsQoL::onModLoad);
        LarielItemRegistration.register(bus);
        LarielCreateModeTabs.register(bus);
        LarielEntityRegistration.register(bus);
    }

    public static void onModLoad(FMLCommonSetupEvent event) {
        var generalConfig = _instance._configManager.general();
        var spawnListener = new LarielPokeSpawnListener(LarielSpawnNotifier.GetInstance());

        if (generalConfig.getEnableSpawnNotification()) {
            NeoForge.EVENT_BUS.register(spawnListener);
        }

        if (generalConfig.getEnableSpawnLevelAdjustment()) {
            Pixelmon.EVENT_BUS.register(spawnListener);
        }

        if (generalConfig.getEnableSpawnNotification()) {
            NeoForge.EVENT_BUS.register(LarielEntityTrackListener.GetInstance());
            NeoForge.EVENT_BUS.register(LarielBlockTrackListener.GetInstance());
        }
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> e.addListener(new ShopkeeperReloadListener()));
        NeoForge.EVENT_BUS.register(new LarielPlayerListener());

        Pixelmon.EVENT_BUS.register(new LarielBetterBreedingListener());
        Pixelmon.EVENT_BUS.register(LarielIWantService.getInstance());
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        // Logic for when the server is starting here
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        // Logic for once the server has started here
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        //Register command logic here
        LarielTrackEntityCommand.register(event.getDispatcher());
        LarielTrackBlockCommand.register(event.getDispatcher());
        LarielResetBreedingCounterCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        // Logic for when the server is stopping
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        // Logic for when the server is stopped
    }

    @SuppressWarnings("unused")
    public static LarielsQoL get_instance() {
        return _instance;
    }

    @SuppressWarnings("unused")
    public static Logger getLogger() {
        return LOGGER;
    }

    public static LarielsQolConfigManager getConfig() {
        return _instance._configManager;
    }

    public void reloadConfig() {
        _configManager = new LarielsQolConfigManager(LOGGER);
        _configManager.loadAll();
    }
}
