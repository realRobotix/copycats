package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;

public class FunctionalCopycatRenderHelper {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer copycatRender(IFunctionalCopycatBlockEntity be) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance()
                .getBlockRenderer();
        return copycatRender(dispatcher.getBlockModel(be.getBlockState()), be, new PoseStack());
    }

    public static SuperByteBuffer getBuffer(IFunctionalCopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                new KineticCopycatRenderData(be.getBlockState(), be.getMaterial(), be.isCTEnabled()),
                () -> copycatRender(be)
        );
    }

    @ExpectPlatform
    public static SuperByteBuffer copycatRender(BakedModel model, IFunctionalCopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}
