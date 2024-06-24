package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public record KineticCopycatRenderData(BlockState state, BlockState material, boolean enableCT) {
    public static KineticCopycatRenderData of(IFunctionalCopycatBlockEntity be) {
        return new KineticCopycatRenderData(be.getBlockState(), be.getMaterial(), be.isCTEnabled());
    }
}
