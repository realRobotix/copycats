package com.copycatsplus.copycats.content.copycat.base.state_switcher;

import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.utility.ItemUtils;
import com.simibubi.create.foundation.gui.ScreenOpener;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

public class StateSwitcherClient {

    public static void onKeyPressed(int key, boolean pressed, LocalPlayer player) {
        if (ItemUtils.isHolding(player, itemStack -> itemStack.is(CCTags.Items.CAN_BE_SWITCHED.tag))) {
            ScreenOpener.open(new StateSwitcherMenu(List.of(CCItems.COPYCAT_BOX.asStack(), CCItems.COPYCAT_CATWALK.asStack())));
        };
    }
}
