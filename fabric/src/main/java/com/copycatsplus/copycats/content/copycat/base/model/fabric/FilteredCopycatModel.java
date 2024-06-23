package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * A Fabric-specific class to implement canConnectTexturesToward and CT toggling logic
 */
public abstract class FilteredCopycatModel extends CopycatModel {
    public FilteredCopycatModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        if (state.getBlock() instanceof CopycatBlock copycatBlock) {
            FilteredBlockAndTintGetter filteredBlockAndTintGetter = new FilteredBlockAndTintGetter(blockView, t -> {
                BlockEntity be = blockView.getBlockEntity(pos);
                if (be instanceof CTCopycatBlockEntity ctbe)
                    if (!ctbe.isCTEnabled())
                        return false;
                return copycatBlock.canConnectTexturesToward(blockView, pos, t, state);
            });
            emitBlockQuadsFiltered(filteredBlockAndTintGetter, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        } else {
            emitBlockQuadsFiltered(blockView, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        }
    }

    protected abstract void emitBlockQuadsFiltered(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData);
}
