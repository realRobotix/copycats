package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties.Side;
import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsEnhancedModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatVerticalStairsEnhancedModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);
        Side side = state.getValue(CopycatVerticalStairBlock.SIDE);

        switch (shape) {
            case STRAIGHT -> {
                boolean flipX = side.isRight();
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).rotateY(facing);
                CopycatStairsEnhancedModel.assembleStraight(context, transform);
            }
            case INNER_BOTTOM, INNER_TOP -> {
                boolean flipY = shape.isTop();
                boolean flipX = side.isRight();
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).flipY(flipY).rotateY(facing);
                CopycatStairsEnhancedModel.assembleInnerLeft(context, transform);
            }
            case OUTER_BOTTOM, OUTER_TOP -> {
                boolean flipY = shape.isTop();
                boolean flipX = side.isRight();
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).flipY(flipY).rotateY(facing);
                CopycatStairsEnhancedModel.assembleOuterLeft(context, transform);
            }
        }
    }
}
