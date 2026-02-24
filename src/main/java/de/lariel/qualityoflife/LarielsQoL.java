package de.lariel.qualityoflife;

import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import de.lariel.qualityoflife.config.LarielsQoLConfig;
import de.lariel.qualityoflife.listener.LarielPokeSpawnListener;
import de.lariel.qualityoflife.utility.LarielSpawnNotifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SuppressWarnings("unused")
@Mod(LarielsQoL.MOD_ID)
@EventBusSubscriber(modid = LarielsQoL.MOD_ID)
public class LarielsQoL {

    public static final String MOD_ID = "larielsqualityoflife";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static LarielsQoL _instance;
    private final LarielSpawnNotifier _notifier;

    private LarielsQoLConfig _config;

    public LarielsQoL(IEventBus bus) {
        _instance = this;
        _notifier = new LarielSpawnNotifier();

        reloadConfig();

        bus.addListener(LarielsQoL::onModLoad);
    }

    public static void onModLoad(FMLCommonSetupEvent event) {
        // Here is how you register a listener for Pixelmon events

        // Since the desired pixelmon event fires to early and the coordinates of the entity is always 0 0 0
        // use the NeoForgeEventBus
        if (_instance._config.GetEnableSpawnNotificationField())
            NeoForge.EVENT_BUS.register(new LarielPokeSpawnListener(_instance._notifier));
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        // Logic for when the server is starting here
    }

    public void reloadConfig() {
        try {
            _config = YamlConfigFactory.getInstance(LarielsQoLConfig.class);
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        // Logic for once the server has started here
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        //Register command logic here
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        // Logic for when the server is stopping
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        // Logic for when the server is stopped
    }

    public static LarielsQoL get_instance() {
        return _instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static LarielsQoLConfig getConfig() {
        return _instance._config;
    }
}
