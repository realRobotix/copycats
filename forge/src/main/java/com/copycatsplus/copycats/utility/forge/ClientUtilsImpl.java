package com.copycatsplus.copycats.utility.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ClientUtilsImpl {

    public static boolean isActiveAndMatches(KeyMapping mapping, InputConstants.Key keyCode) {
        return mapping.isActiveAndMatches(keyCode);
    }
}
