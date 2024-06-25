package com.copycatsplus.copycats.content.copycat.base.model.functional.forge;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class FunctionalCopycatRenderHelperImpl {

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, IFunctionalCopycatBlockEntity be, PoseStack ms) {
        ModelData renderData = model.getModelData(be.getLevel(), be.getBlockPos(), be.getBlockState(), be.getCopycatBlockEntity().getModelData());
        ModelData.Builder builder = ModelData.builder();
        copyModelData(renderData, builder);
        builder.with(ModelUtil.VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(be.getLevel())
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }

    public static ModelData.Builder mergeData(ModelData data1, ModelData data2) {
        ModelData.Builder builder = ModelData.builder();
        copyModelData(data1, builder);
        copyModelData(data2, builder);
        return builder;
    }

    static void copyModelData(ModelData from, ModelData.Builder to) {
        for (ModelProperty<?> property : from.getProperties()) {
            copyModelProperty(to, from, property);
        }
    }

    static <T> void copyModelProperty(ModelData.Builder to, ModelData from, ModelProperty<T> property) {
        to.with(property, from.get(property));
    }
}
