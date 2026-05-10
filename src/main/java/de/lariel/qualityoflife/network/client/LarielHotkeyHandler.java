package de.lariel.qualityoflife.network.client;

import de.lariel.qualityoflife.keybinds.LarielKeybinds;
import de.lariel.qualityoflife.network.packet.LarielOpenPokeBagPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class LarielHotkeyHandler {
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        HandleOpenPokeBag(event);
    }

    private void HandleOpenPokeBag(ClientTickEvent.Post event) {
        while (LarielKeybinds.OPEN_POKE_BAG_HOTKEY.get().consumeClick()) {
            PacketDistributor.sendToServer(new LarielOpenPokeBagPacket(true));
        }
    }
}
