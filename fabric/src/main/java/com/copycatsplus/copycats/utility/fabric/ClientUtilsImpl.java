package com.copycatsplus.copycats.utility.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.fabricators_of_create.porting_lib.util.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class ClientUtilsImpl {


    public static boolean isActiveAndMatches(KeyMapping mapping, InputConstants.Key keyCode) {
        return KeyBindingHelper.isActiveAndMatches(mapping, keyCode);
    }
}
