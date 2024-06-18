package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CopycatsClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class CopycatsClientImpl {

    public static void init(IEventBus bus) {
        CopycatsClient.init();
        bus.addListener(CCKeysImpl::onRegisterKeyMappings);
        MinecraftForge.EVENT_BUS.addListener(CopycatsClientImpl::onKeyInput);
    }

    private static void onKeyInput(InputEvent.Key event) {
        int key = event.getKey();
        boolean pressed = event.getAction() != 0;
        CopycatsClient.onKeyPressed(key, pressed);
    }
}
