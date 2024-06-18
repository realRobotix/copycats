package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemUtils {

    @ExpectPlatform
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        throw new AssertionError("This should never appear!");
    }

    public static boolean isHolding(Player player, Predicate<ItemStack> predicate) {
        return predicate.test(player.getItemInHand(InteractionHand.MAIN_HAND))
                || predicate.test(player.getItemInHand(InteractionHand.OFF_HAND));
    }
}
