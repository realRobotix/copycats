package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.world.level.block.state.BlockState;

public enum StateType {
    SINGULAR,
    MULTI;

    StateType getTypeFromState(BlockState state) {
        if (state.getBlock() instanceof IStateType stateType) {
            return stateType.stateType();
        } else {
            return SINGULAR;
        }
    }
}
