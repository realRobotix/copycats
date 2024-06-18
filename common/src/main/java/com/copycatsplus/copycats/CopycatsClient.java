package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.state_switcher.StateSwitcherClient;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.network.CCPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class CopycatsClient {

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        CCPackets.PACKETS.registerS2CListener();

        CCKeys.register();
    }

    public static void onKeyPressed(int key, boolean pressed) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        LocalPlayer player = mc.player;
        if (player == null)
            return;

        if (key == CCKeys.STATE_SWITCHER_MENU.getBoundCode() && pressed)
            StateSwitcherClient.onKeyPressed(key, pressed, player);
    }
}
