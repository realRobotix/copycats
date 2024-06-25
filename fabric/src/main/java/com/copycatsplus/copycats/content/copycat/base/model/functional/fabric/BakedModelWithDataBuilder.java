package com.copycatsplus.copycats.content.copycat.base.model.functional.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.functional.WrappedRenderWorld;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.core.model.Bufferable;
import com.jozufozu.flywheel.core.model.ShadeSeparatingVertexConsumer;
import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.jozufozu.flywheel.fabric.model.DefaultLayerFilteringBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BakedModelWithDataBuilder implements Bufferable {
    private final BakedModel model;
    private BlockAndTintGetter renderWorld = VirtualEmptyBlockGetter.INSTANCE;
    private BlockState referenceState = Blocks.AIR.defaultBlockState();
    private PoseStack poseStack = new PoseStack();
    private BlockPos renderPos = BlockPos.ZERO;

    public BakedModelWithDataBuilder(BakedModel model) {
        this.model = model;
    }

    public BakedModelWithDataBuilder withRenderPos(BlockPos renderPos) {
        this.renderPos = renderPos;
        return this;
    }

    public BakedModelWithDataBuilder withRenderWorld(BlockAndTintGetter renderWorld) {
        this.renderWorld = new WrappedRenderWorldFabric(renderWorld);
        return this;
    }

    public BakedModelWithDataBuilder withReferenceState(BlockState referenceState) {
        this.referenceState = referenceState;
        return this;
    }

    public BakedModelWithDataBuilder withPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
        return this;
    }

    @Override
    public void bufferInto(VertexConsumer consumer, ModelBlockRenderer blockRenderer, RandomSource random) {
        BakedModel model = DefaultLayerFilteringBakedModel.wrap(this.model);
        if (consumer instanceof ShadeSeparatingVertexConsumer shadeSeparatingWrapper) {
            model = shadeSeparatingWrapper.wrapModel(model);
        }
        blockRenderer.tesselateBlock(renderWorld, model, referenceState, renderPos, poseStack, consumer, false, random, 42, OverlayTexture.NO_OVERLAY);
    }

    public BlockModel toModel(String name) {
        return BlockModel.of(this, name);
    }

    public BlockModel toModel() {
        return toModel(referenceState.toString());
    }

    @SuppressWarnings("deprecation")
    private static class WrappedRenderWorldFabric extends WrappedRenderWorld implements RenderAttachedBlockView {
        public WrappedRenderWorldFabric(BlockAndTintGetter level) {
            super(level);
        }

        @Override
        @Nullable
        public Object getBlockEntityRenderAttachment(@NotNull BlockPos pos) {
            return ((RenderAttachedBlockView) level).getBlockEntityRenderAttachment(pos);
        }
    }
}
