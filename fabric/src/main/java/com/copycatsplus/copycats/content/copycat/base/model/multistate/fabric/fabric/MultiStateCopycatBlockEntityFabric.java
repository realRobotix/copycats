package com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MultiStateCopycatBlockEntityFabric extends MultiStateCopycatBlockEntity {
    public MultiStateCopycatBlockEntityFabric(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void requestModelUpdate() {

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

}
