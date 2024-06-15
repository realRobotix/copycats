package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatVerticalStairsModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).rotateY(facing);
                CopycatStairsModel.assembleStraight(context, transform);
            }
            case INNER_BOTTOM, INNER_TOP -> {
                boolean flipY = shape == VerticalStairShape.INNER_TOP;
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).flipY(flipY).rotateY(facing);
                CopycatStairsModel.assembleInnerLeft(context, transform);
            }
            case OUTER_BOTTOM_LEFT, OUTER_BOTTOM_RIGHT, OUTER_TOP_LEFT, OUTER_TOP_RIGHT -> {
                boolean flipY = shape == VerticalStairShape.OUTER_TOP_LEFT || shape == VerticalStairShape.OUTER_TOP_RIGHT;
                boolean flipX = shape == VerticalStairShape.OUTER_BOTTOM_RIGHT || shape == VerticalStairShape.OUTER_TOP_RIGHT;
                GlobalTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).flipY(flipY).rotateY(facing);
                CopycatStairsModel.assembleOuterLeft(context, transform);
            }
        }
    }
}
