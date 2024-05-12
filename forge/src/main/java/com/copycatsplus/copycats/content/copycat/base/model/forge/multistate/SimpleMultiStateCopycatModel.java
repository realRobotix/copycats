package com.copycatsplus.copycats.content.copycat.base.model.forge.multistate;

import com.copycatsplus.copycats.content.copycat.base.model.QuadHelper;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.MultiStateQuadHelper;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleMultiStateCopycatModel extends MultiStateCopycatModel {

    private final SimpleMultiStateCopycatPart part;
    public SimpleMultiStateCopycatModel(BakedModel originalModel, SimpleMultiStateCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, Map<String, BlockState> propertyMaterials, ModelData wrappedData, RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        List<List<BakedQuad>> templateQuads = new ArrayList<>();
        for (Map.Entry<String, BlockState> pM : propertyMaterials.entrySet()) {
            BakedModel model = getModelOf(pM.getValue());
            templateQuads.add(model.getQuads(pM.getValue(), side, rand, wrappedData, renderType));
        }
        MultiStateQuadHelper.CopycatRenderContext<List<BakedQuad>, List<BakedQuad>> context = new MultiStateQuadHelper.CopycatRenderContext<>(templateQuads, quads);

        part.emitCopycatQuads(state, context, propertyMaterials);

        return quads;
    }
}
