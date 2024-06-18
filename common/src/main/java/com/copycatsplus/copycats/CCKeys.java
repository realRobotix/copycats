package com.copycatsplus.copycats;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
public enum CCKeys {
    STATE_SWITCHER_MENU("state_switcher_menu", GLFW.GLFW_KEY_V);

    private KeyMapping keyMapping;
    private final String id;
    private final int defaultKey;
    private final boolean modifiable;

    CCKeys(String id, int defaultKey) {
        this.id = Copycats.MODID + ".key." + id;
        this.defaultKey = defaultKey;
        this.modifiable = !id.isEmpty();
    }

    public static void register() {
        for (CCKeys key : values()) {
            if (!key.modifiable)
                continue;
            key.keyMapping = new KeyMapping(key.id, key.defaultKey, "key.categories." + Copycats.MODID);
            registerKeyBinding(key.keyMapping);
        }
    }

    public static void fixBinds() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        for (CCKeys key : values()) {
            if (key.keyMapping == null || key.keyMapping.isUnbound())
                continue;
            key.keyMapping.setDown(InputConstants.isKeyDown(window, key.getBoundCode()));
        }
    }

    public KeyMapping getKeyMapping() {
        return keyMapping;
    }

    public boolean isPressed() {
        if (!modifiable)
            return isKeyDown(defaultKey);
        return keyMapping.isDown();
    }

    public int getBoundCode() {
        return getBoundCode(keyMapping);
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key);
    }

    @ExpectPlatform
    private static void registerKeyBinding(KeyMapping keyMapping) {
        throw new AssertionError();
    }

    @ExpectPlatform
    private static int getBoundCode(KeyMapping keyMapping) {
        throw new AssertionError();
    }

}
