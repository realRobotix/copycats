package com.copycatsplus.copycats.content.copycat.base.model.functional.fabric;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;

public class FunctionalCopycatRenderHelperImpl {
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, IFunctionalCopycatBlockEntity be, PoseStack ms) {
        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(be.getLevel())
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .build();
    }
}
