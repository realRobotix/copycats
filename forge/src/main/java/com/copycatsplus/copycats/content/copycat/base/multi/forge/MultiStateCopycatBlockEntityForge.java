package com.copycatsplus.copycats.content.copycat.base.multi.forge;

import com.copycatsplus.copycats.content.copycat.base.multi.MultiStateCopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MultiStateCopycatBlockEntityForge extends MultiStateCopycatBlockEntity {
    public MultiStateCopycatBlockEntityForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void requestModelUpdate() {
        requestModelDataUpdate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
