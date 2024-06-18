package com.copycatsplus.copycats.utility;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;

public class ClientUtils {

    @ExpectPlatform
    public static boolean isActiveAndMatches(KeyMapping mapping, InputConstants.Key keyCode) {
        throw new AssertionError("This should never appear");
    }
}
