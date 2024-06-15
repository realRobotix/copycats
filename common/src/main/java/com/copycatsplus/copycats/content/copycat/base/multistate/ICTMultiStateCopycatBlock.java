package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.ICTCopycatBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface ICTMultiStateCopycatBlock extends IBE<MultiStateCopycatBlockEntity> {

    default boolean allowCTAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        MultiStateCopycatBlockEntity be = getBlockEntity(level, pos);
        if (be instanceof CTCopycatBlockEntity ctbe) {
            if (!ctbe.isCTEnabled()) {
                return queryState.getBlock() instanceof ICTCopycatBlock;
            }
        }
        return true;
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isCrouching() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            MultiStateCopycatBlockEntity be = getBlockEntity(pLevel, pPos);
            if (be instanceof CTCopycatBlockEntity ctbe) {
                ctbe.setCTEnabled(!ctbe.isCTEnabled());
                ctbe.callRedraw();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}