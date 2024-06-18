package com.copycatsplus.copycats.fabric;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class CCKeysImpl {

    public static void registerKeyBinding(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    public static int getBoundCode(KeyMapping keyMapping) {
        return KeyBindingHelper.getBoundKeyOf(keyMapping).getValue();
    }
}
