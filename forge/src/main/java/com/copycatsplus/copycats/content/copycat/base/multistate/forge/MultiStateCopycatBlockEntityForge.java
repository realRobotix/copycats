package com.copycatsplus.copycats.content.copycat.base.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.forge.MultiStateCopycatModel;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull IModelData getModelData() {
         return new ModelDataMap.Builder()
                .withInitial(MultiStateCopycatModel.MATERIALS_PROPERTY, getMaterialItemStorage().getMaterialMap())
                .build();
    }
}
