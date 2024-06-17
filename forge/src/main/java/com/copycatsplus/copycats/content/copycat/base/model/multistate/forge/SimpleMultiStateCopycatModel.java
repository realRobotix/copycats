package com.copycatsplus.copycats.content.copycat.base.model.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.forge.AssemblerImpl;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.forge.AssemblerImpl.CopycatRenderContextForge;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleMultiStateCopycatModel extends MultiStateCopycatModel {

    private final SimpleMultiStateCopycatPart part;
    public SimpleMultiStateCopycatModel(BakedModel originalModel, SimpleMultiStateCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(String key, BlockState state, Direction side, Random rand, BlockState material, IModelData wrappedData, RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        List<BakedQuad> templateQuads = getModelOf(material).getQuads(material, side, rand, wrappedData);
        CopycatRenderContextForge context = new CopycatRenderContextForge(templateQuads, quads);

        part.emitCopycatQuads(key, state, context, material);

        return quads;
    }
}
